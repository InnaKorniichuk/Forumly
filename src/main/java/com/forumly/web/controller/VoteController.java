package com.forumly.web.controller;

import com.forumly.web.model.User;
import com.forumly.web.model.VoteType;
import com.forumly.web.service.CommentService;
import com.forumly.web.service.VoteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/votes")
public class VoteController {
    private final VoteService voteService;
    private final CommentService commentService;
    public VoteController(VoteService voteService, CommentService commentService){
        this.voteService=voteService;
        this.commentService=commentService;
    }

    @PostMapping("/posts/{post_id}/add")
    public String addVoteToPost(@PathVariable("post_id") Long postId,
                                @AuthenticationPrincipal User currentUser,
                                VoteType voteType){
        voteService.addVoteToPost(currentUser.getId(),postId,voteType);
        return "redirect:/posts/"+postId+"/read";
    }

    @PostMapping("/comments/{comment_id}/add")
    public String addVoteToComment(@PathVariable("comment_id") Long commentId,
                                   @AuthenticationPrincipal User currentUser,
                                   VoteType voteType){
        voteService.addVoteToComment(currentUser.getId(),commentId,voteType);
        Long postId = commentService.readById(commentId).getPost().getId();
        return "redirect:/posts/"+postId+"/read";
    }

    @PostMapping("/posts/{post_id}/remove")
    public String removeVoteFromPost(@PathVariable("post_id") Long postId,
                                     @AuthenticationPrincipal User currentUser){
        voteService.removeVoteFromPost(currentUser.getId(), postId);
        return "redirect:/posts/"+postId+"/read";
    }

    @PostMapping("/comments/{comment_id}/remove")
    public String removeVoteFromComment(@PathVariable("comment_id") Long commentId,
                                        @AuthenticationPrincipal User currentUser){
        voteService.removeVoteFromComment(currentUser.getId(),commentId);
        Long postId = commentService.readById(commentId).getPost().getId();
        return "redirect:/posts/"+postId+"/read";
    }
}
