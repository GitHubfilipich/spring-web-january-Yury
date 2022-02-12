package ru.specialist.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.entity.User;
import ru.specialist.spring.repository.PostRepository;
import ru.specialist.spring.repository.UserRepository;
import ru.specialist.spring.service.PostService;
import ru.specialist.spring.service.UserService;
import ru.specialist.spring.util.SecurityUtils;

import javax.servlet.ServletContext;

@Controller
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostService postService;
    private final ServletContext servletContext;

    @Autowired
    public PostController(PostRepository postRepository,
                          UserRepository userRepository,
                          UserService userService,
                          PostService postService,
                          ServletContext servletContext) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.postService = postService;
        this.servletContext = servletContext;
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
        Long postId = postService.create(postDto);
        return "redirect:/post/" + postId;
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
        return "redirect:/post/" + postDto.getPostId();
    }

    @GetMapping("/post/{postId}")
    public String post(@PathVariable Long postId, ModelMap model){
        model.put("post", postService.findById(postId));
        setCommonParams(model);
        return "post";
    }

    @PostMapping("/post/{postId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void postDelete(@PathVariable Long postId){
        postService.delete(postId);
    }



    private void setCommonParams(ModelMap model) {
        model.put("users", userRepository.findAll());
        model.put("contextPath", servletContext.getContextPath());
    }
}
