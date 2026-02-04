package com.forumly.web.controller;

import com.forumly.web.dto.comment.CommentRequestDto;
import com.forumly.web.dto.comment.CommentResponseDto;
import com.forumly.web.model.Comment;
import com.forumly.web.model.User;
import com.forumly.web.service.CommentService;
import com.forumly.web.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts/{post_id}/comments")
public class CommentController {
    private final CommentService commentService;
    private final VoteService voteService;
    public CommentController(CommentService commentService, VoteService voteService){
        this.commentService = commentService;
        this.voteService = voteService;
    }

   @PostMapping("/create")
    public String create(@PathVariable("post_id") Long postId,
                         @Valid @ModelAttribute("comment") CommentRequestDto dto,
                         Model model,
                         @AuthenticationPrincipal User currentUser,
                         BindingResult result){
        if(result.hasErrors())
            return "redirect:/posts/"+postId+"/read";

       Comment comment = commentService.create(dto, currentUser.getId(), postId);
       return "redirect:/posts/" + postId;
   }

   @GetMapping("/{id}")
   public String read(@PathVariable Long id, Model model){
       CommentResponseDto comment = commentService.readById(id);
       model.addAttribute("comment", comment);
       return "read-comment";
   }

   @GetMapping("/{id}/delete")
   public String delete(@PathVariable Long id,
                        @AuthenticationPrincipal User currentUser){
        commentService.delete(id,currentUser.getId());
        return "redirect:/posts/{postId}";
   }

   @GetMapping("/{id}/update")
   public String update(@PathVariable Long id, Model model){
        model.addAttribute("commentInfo",commentService.readById(id));
        model.addAttribute("commentUpdate", new CommentRequestDto());
        return "update-comment";
   }

   @PostMapping("/{id}/update")
   public String update(@PathVariable Long id,
                        @PathVariable("post_id") Long postId,
                       @Valid @ModelAttribute("comment")CommentRequestDto dto,
                        @AuthenticationPrincipal User currentUser,
                        BindingResult result,
                        Model model){
        if(result.hasErrors()){
            model.addAttribute("comment", commentService.readById(id));
            return "update-comment";
        }

        commentService.update(id, dto, currentUser.getId());
        return "redirect:/posts/"+postId+"/read";
   }

   @GetMapping()
   public String getAllByPost(@PathVariable("post_id") Long postId,
                              Model model){
        model.addAttribute("comments", commentService.getByPostId(postId));
        return "all-comments";
   }
}
