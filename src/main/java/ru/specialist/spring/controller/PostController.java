package ru.specialist.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.entity.User;
import ru.specialist.spring.repository.PostRepository;
import ru.specialist.spring.repository.UserRepository;
import ru.specialist.spring.service.PostService;
import ru.specialist.spring.service.UserService;
import ru.specialist.spring.util.SecurityUtils;

@Controller
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public PostController(PostRepository postRepository,
                          UserRepository userRepository,
                          UserService userService,
                          PostService postService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public String posts(@RequestParam(name = "q", required = false) String query,
                        ModelMap model) {
        if (StringUtils.hasText(query)) {
            model.put("posts",
                    postRepository.findByContentContainingIgnoreCase(query,
                            Sort.by("dtCreated").descending()));
            model.put("title", "Search by");
            model.put("subtitle", query.length() < 20
                    ? query
                    : query.substring(0, 20) + "...");
        } else {
            model.put("posts", postRepository.findAll(Sort.by("dtCreated").descending()));
            model.put("title", "All posts");
        }

        setCommonParams(model);
        return "blog";
    }

    @GetMapping("/user/{username}")
    public String postsByUser(@PathVariable String username, ModelMap model) {
        User user = userService.findByUsername(username);
        model.put("posts", user.getPosts());
        model.put("title", "Search by");
        model.put("subtitle", username);

        setCommonParams(model);
        return "blog";
    }

    @GetMapping("/post/new")
    @PreAuthorize("hasRole('USER')")
    public String postNew(ModelMap model){
        setCommonParams(model);
        return "post-new";
    }

    @PostMapping("/post/new")
    @PreAuthorize("hasRole('USER')")
    public String postNew(PostDto postDto){
        postService.create(postDto);
        return "redirect:/";
    }


    @GetMapping("/post/{postId}/edit")
    @PreAuthorize("hasRole('USER')")
    public String postEdit(@PathVariable Long postId, ModelMap model){
        Post post = postService.findById(postId);
        SecurityUtils.checkAuthorityOnPost(post);

        model.put("post", new PostDto(post));
        setCommonParams(model);
        return "post-edit";
    }

    @PostMapping("/post/edit")
    @PreAuthorize("hasRole('USER')")
    public String postEdit(PostDto postDto){
        postService.update(postDto);
        return "redirect:/";
    }




    private void setCommonParams(ModelMap model) {
        model.put("users", userRepository.findAll());
    }
}
