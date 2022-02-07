package ru.specialist.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.entity.User;
import ru.specialist.spring.repository.PostRepository;
import ru.specialist.spring.repository.UserRepository;
import ru.specialist.spring.service.UserService;

import java.util.List;

@Controller
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public PostController(PostRepository postRepository,
                          UserRepository userRepository,
                          UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public String posts(@RequestParam(required = false) String search, ModelMap model){
        if (search != null && !search.isEmpty()){
            List<Post> posts = postRepository.findPostsByContentSubString("%" + search.trim().toLowerCase() + "%");
            model.put("posts", posts);
            model.put("subtitle", "Blogs contaning: '" + search.trim() + "' (" + posts.size() + ")");
        }
            else {
            model.put("posts", postRepository.findAll());
        }

        setCommonParams(model);
        return "blog";
    }

    @GetMapping("/user/{username}")
    public String postsByUser(@PathVariable String username, ModelMap model){
        User user = userService.findByUsername(username);
        model.put("posts", user.getPosts());
        setCommonParams(model);
        return "blog";
    }

    private void setCommonParams(ModelMap model){
        model.put("users", userRepository.findAll());
    }
}
