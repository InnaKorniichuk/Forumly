package com.forumly.web.controller;

import com.forumly.web.model.User;
import com.forumly.web.model.VoteType;
import com.forumly.web.service.CommentService;
import com.forumly.web.service.UserService;
import com.forumly.web.service.VoteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/votes")
public class VoteController {
    private final VoteService voteService;
    private final CommentService commentService;
    private final UserService userService;

    public VoteController(VoteService voteService, CommentService commentService, UserService userService){
        this.voteService = voteService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/posts/{post_id}/add")
    public String addVoteToPost(
            @PathVariable("post_id") Long postId,
            @RequestParam("voteType") VoteType voteType,
            Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        voteService.votePost(currentUser.getId(), postId, voteType);

        return "redirect:/communities/" + postId + "/read";
    }

    @PostMapping("/comments/{comment_id}/add")
    public String addVoteToComment(
            @PathVariable("comment_id") Long commentId,
            @RequestParam("voteType") VoteType voteType,
            Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        voteService.vote(currentUser.getId(), commentId, voteType);

        Long postId = commentService.readById(commentId)
                .getPost()
                .getId();

        return "redirect:/posts/" + postId + "/read";
    }

    @PostMapping("/posts/{post_id}/remove")
    public String removeVoteFromPost(@PathVariable("post_id") Long postId){
        return "redirect:/posts/"+postId+"/read";
    }

    @PostMapping("/comments/{comment_id}/remove")
    public String removeVoteFromComment(@PathVariable("comment_id") Long commentId){
        Long postId = commentService.readById(commentId).getPost().getId();
        return "redirect:/posts/"+postId+"/read";
    }
}
