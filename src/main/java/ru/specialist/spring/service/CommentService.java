package ru.specialist.spring.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.entity.Comment;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.repository.CommentRepository;
import ru.specialist.spring.repository.PostRepository;
import ru.specialist.spring.repository.UserRepository;

import java.time.LocalDateTime;

import static ru.specialist.spring.util.SecurityUtils.getCurrentUserDetails;

@Service
@Transactional
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @PreAuthorize("hasRole('USER')")
    public void create(PostDto postDto, String content) {
        Comment comment = new Comment();
        comment.setPost(postRepository.findById(postDto.getPostId()).orElseThrow());
        comment.setUser(userRepository.findByUsername(getCurrentUserDetails()
            .getUsername()).orElseThrow());
        comment.setContent(content);
        comment.setDtCreated(LocalDateTime.now());

        commentRepository.save(comment);
    }
}
