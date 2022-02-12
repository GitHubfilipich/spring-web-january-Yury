package ru.specialist.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.specialist.spring.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String signUp(){
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(String username, String password){
        userService.create(username, password);
        return "redirect:/sign-in";
    }
}
