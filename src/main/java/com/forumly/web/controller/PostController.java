package com.forumly.web.controller;

import com.forumly.web.dto.post.PostRequestDto;
import com.forumly.web.dto.post.PostResponseDto;
import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.Post;
import com.forumly.web.model.Role;
import com.forumly.web.model.User;
import com.forumly.web.service.PostService;
import com.forumly.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/create/{community_id}")
    public String createForm(@PathVariable("community_id") Long communityId, Model model) {
        model.addAttribute("post", new PostRequestDto());
        model.addAttribute("community_id", communityId);
        return "post/create";
    }

    @PostMapping("/create/{community_id}")
    public String create(@PathVariable("community_id") Long communityId,
                         @Valid @ModelAttribute("post") PostRequestDto dto,
                         BindingResult result,
                         Principal principal,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("community_id", communityId);
            return "post/create";
        }

        String email = principal.getName();
        User currentUser = userService.findByEmail(email);
        Post post = postService.create(dto, communityId, currentUser.getId());
        return "redirect:/posts/" + post.getId() + "/read";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        if (!currentUser.getRole().equals(Role.ADMIN)
                && !currentUser.getId().equals(postService.readById(id).getAuthor().getId()))
            throw ApplicationException.unauthorized("You are not allowed to delete this post");

        postService.delete(id, currentUser.getId());
        return "redirect:/communities/getAll";
    }

    @GetMapping("/{id}/read")
    public String read(@PathVariable Long id, Model model) {
        PostResponseDto postResponseDto = postService.readById(id);
        model.addAttribute("post", postResponseDto);
        return "post/read";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         Model model,
                         Principal principal) {
        String email = principal.getName();
        User currentUser = userService.findByEmail(email);

        if (!currentUser.getId().equals(postService.readById(id).getAuthor().getId())
                && !currentUser.getRole().equals(Role.ADMIN))
            throw ApplicationException.unauthorized("You are not allowed to update this post");

        PostResponseDto post = postService.readById(id);
        model.addAttribute("postInfo", post);
        model.addAttribute("postUpdate", new PostRequestDto());

        return "post/update";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("postUpdate") PostRequestDto dto,
                         BindingResult result,
                         Model model,
                         Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute("postInfo", postService.readById(id));
            return "post/update";
        }

        String email = principal.getName();
        User currentUser = userService.findByEmail(email);
        postService.update(id, dto, currentUser.getId());

        return "redirect:/posts/" + id + "/read";
    }
}
