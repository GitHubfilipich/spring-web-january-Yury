package ru.specialist.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.repository.CommentRepository;
import ru.specialist.spring.service.CommentService;

@Controller
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentRepository commentRepository, CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/create")
    @PreAuthorize("hasRole('USER')")
    public String commentCreate(@RequestParam(name = "content", required = false) String content, PostDto postDto){
        if (StringUtils.hasText(content)){
            commentService.create(postDto, content);
        }

        return "redirect:/post/" + postDto.getPostId();
    }











}
