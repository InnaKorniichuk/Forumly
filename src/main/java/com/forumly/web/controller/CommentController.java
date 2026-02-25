package com.forumly.web.controller;

import com.forumly.web.dto.comment.CommentRequestDto;
import com.forumly.web.dto.post.PostResponseDto;
import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.Comment;
import com.forumly.web.model.Role;
import com.forumly.web.model.User;
import com.forumly.web.service.CommentService;
import com.forumly.web.service.PostService;
import com.forumly.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/posts/{post_id}/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    public CommentController(CommentService commentService, PostService postService, UserService userService){
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

   @PostMapping("/create")
    public String create(@PathVariable("post_id") Long postId,
                         @Valid @ModelAttribute("comment") CommentRequestDto dto,
                         Model model,
                         Principal principal,
                         BindingResult result){
        if(result.hasErrors())
            return "redirect:/posts/"+postId+"/read";

       String email = principal.getName();
       User currentUser = userService.findByEmail(email);
       Comment comment = commentService.create(dto, currentUser.getId(), postId);

       return "redirect:/posts/" + postId + "/read";
   }

   @GetMapping("/{id}/delete")
   public String delete(@PathVariable Long id,
                        Principal principal,
                        Model model){
        String email = principal.getName();
        User currentUser = userService.findByEmail(email);

       if(!currentUser.getId().equals(commentService.readById(id).getAuthor().getId())
               && !currentUser.getRole().equals(Role.ADMIN))
           throw ApplicationException.unauthorized("You are not allowed to delete this comment");

       commentService.delete(id,currentUser.getId());
       PostResponseDto postResponseDto = postService.readById(id);
       model.addAttribute("post",postResponseDto);
       return "post/read";
   }

   @PostMapping("/{id}/update")
   public String update(@PathVariable Long id,
                        @PathVariable("post_id") Long postId,
                       @Valid @ModelAttribute("comment")CommentRequestDto dto,
                        Principal principal,
                        BindingResult result,
                        Model model){
        if(result.hasErrors()){
            model.addAttribute("comment", commentService.readById(id));
            return "redirect:/posts/"+postId+"/read";
        }

        String email = principal.getName();
        User currentUser = userService.findByEmail(email);
       if(!currentUser.getId().equals(commentService.readById(id).getAuthor().getId()))
           throw ApplicationException.unauthorized("You are not allowed to edit this comment");

        commentService.update(id, dto, currentUser.getId());
        return "redirect:/posts/"+postId+"/read";
   }
}
