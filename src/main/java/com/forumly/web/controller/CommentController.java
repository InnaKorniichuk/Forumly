package com.forumly.web.controller;

import com.forumly.web.dto.comment.CommentRequestDto;
import com.forumly.web.model.User;
import com.forumly.web.service.CommentService;
import com.forumly.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/posts/{post_id}/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService){
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public String create(@PathVariable("post_id") Long postId,
                         @Valid @ModelAttribute("comment") CommentRequestDto dto,
                         BindingResult result,
                         Principal principal) {
        if (result.hasErrors())
            return "redirect:/posts/" + postId + "/read";

        User currentUser = userService.findByEmail(principal.getName());
        commentService.create(dto, currentUser.getId(), postId);

        return "redirect:/posts/" + postId + "/read";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("post_id") Long postId,
                         @PathVariable Long id,
                         Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        commentService.delete(id, currentUser.getId());
        return "redirect:/posts/" + postId + "/read";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @PathVariable("post_id") Long postId,
                         @Valid @ModelAttribute("comment") CommentRequestDto dto,
                         BindingResult result,
                         Principal principal) {
        if (result.hasErrors())
            return "redirect:/posts/" + postId + "/read";

        User currentUser = userService.findByEmail(principal.getName());
        commentService.update(id, dto, currentUser.getId());
        return "redirect:/posts/" + postId + "/read";
    }
}
