package com.forumly.web.controller;

import com.forumly.web.dto.post.PostRequestDto;
import com.forumly.web.dto.post.PostResponseDto;
import com.forumly.web.dto.post.PostUpdateDto;
import com.forumly.web.model.Post;
import com.forumly.web.model.User;
import com.forumly.web.service.PostService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/create")
    public String createForm(@RequestParam("community_id") Long communityId, Model model) {
        model.addAttribute("post", new PostRequestDto());
        model.addAttribute("community_id", communityId);
        return "create-post";
    }

    @PostMapping("/create")
    public String create(@RequestParam("community_id") Long communityId,
                         @Valid @ModelAttribute("post") PostRequestDto dto,
                         BindingResult result,
                         @AuthenticationPrincipal User currentUser,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("community_id", communityId);
            return "create-post";
        }

        Post post = postService.create(dto, communityId, currentUser.getId());
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/{user_id}/{id}/delete")
    public String delete(@PathVariable Long id,
                         @PathVariable("user_id") Long userId){
        postService.delete(id,userId);
        return "all-posts";
    }

    @GetMapping("/{id}")
    public String read(@PathVariable Long id, Model model){
        PostResponseDto postResponseDto = postService.readById(id);
        model.addAttribute("post",postResponseDto);
        return "read-post";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id, Model model){
        PostResponseDto post = postService.readById(id);
        model.addAttribute("postInfo", post);
        model.addAttribute("postUpdate", new PostUpdateDto());

        return "update-post";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("postUpdate") PostUpdateDto dto,
                         BindingResult result,
                         Model model,
                         @AuthenticationPrincipal User currentUser) {

        if (result.hasErrors()) {
            model.addAttribute("postInfo", postService.readById(id));
            return "update-post";
        }

        postService.update(id, dto, currentUser.getId());

        return "redirect:/posts/" + id + "/read";
    }

    @GetMapping("/community/{community_id}")
    public String getPostsByCommunity(@PathVariable Long community_id, Model model){
        model.addAttribute("posts", postService.getByCommunityId(community_id));
        return "all-posts";
    }

    @GetMapping("/user/{user_id}")
    public String getPostsByUser(@PathVariable Long user_id, Model model){
        model.addAttribute("posts", postService.getByAuthorId(user_id));
        return "all-posts";
    }
}
