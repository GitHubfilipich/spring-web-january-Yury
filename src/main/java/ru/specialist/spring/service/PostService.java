package ru.specialist.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.entity.Tag;
import ru.specialist.spring.repository.PostRepository;
import ru.specialist.spring.repository.TagRepository;
import ru.specialist.spring.repository.UserRepository;
import ru.specialist.spring.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.specialist.spring.util.SecurityUtils.*;

@Service
@Transactional
public class PostService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(TagRepository tagRepository,
                       UserRepository userRepository,
                       PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @PreAuthorize("hasRole('USER')")
    public Long create(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setTags(parseTags(postDto.getTags()));
        post.setUser(userRepository.findByUsername(getCurrentUserDetails()
                        .getUsername())
                .orElseThrow());
        post.setDtCreated(LocalDateTime.now());
        return postRepository.save(post).getPostId();
    }

    private List<Tag> parseTags(String tags) {
        if (tags == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(tags.split(" "))
                .map(tag -> tagRepository.findByName(tag).orElseGet(
                        () -> tagRepository.save(new Tag(tag))))
                .collect(Collectors.toList());
    }

    public Post findById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.getTags().size();
        return post;
    }

    @PreAuthorize("hasRole('USER')")
    public void update(PostDto postDto) {
        Post post = postRepository.findById(postDto.getPostId()).orElseThrow();
        SecurityUtils.checkAuthorityOnPost(post);

        if (postDto.getTitle() != null) {
            post.setTitle(StringUtils.hasText(postDto.getTitle())
                    ? postDto.getTitle()
                    : "");
        }

        if (postDto.getContent() != null) {
            post.setContent(StringUtils.hasText(postDto.getContent())
                    ? postDto.getContent()
                    : "");
        }

        if (postDto.getTags() != null) {
            post.setTags(StringUtils.hasText(postDto.getTags())
                    ? parseTags(postDto.getTags())
                    : Collections.emptyList());
        }

        post.setDtUpdated(LocalDateTime.now());
        postRepository.save(post);
    }

    public void delete(Long postId) {
        checkAuthorityOnPostOrUserIsAdmin(
                postRepository.findById(postId).orElseThrow());
        postRepository.deleteById(postId);
    }
}
