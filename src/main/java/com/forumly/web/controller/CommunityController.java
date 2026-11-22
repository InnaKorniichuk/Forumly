package com.forumly.web.controller;

import com.forumly.web.dto.community.CommunityRequestDto;
import com.forumly.web.dto.community.CommunityResponseDto;
import com.forumly.web.model.User;
import com.forumly.web.service.CommunityService;
import com.forumly.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/communities")
public class CommunityController {
    private final CommunityService communityService;
    private final UserService userService;

    public CommunityController(CommunityService communityService, UserService userService){
        this.communityService = communityService;
        this.userService=userService;
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("community", new CommunityRequestDto());
        return "community/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("community") CommunityRequestDto communityDto,
                         BindingResult result,
                         Principal principal) {
        if (result.hasErrors()) {
            return "community/create";
        }

        User currentUser = userService.findByEmail(principal.getName());
        communityService.create(communityDto, currentUser.getId());

        return "redirect:/communities/getAll";
    }

    @GetMapping("/{id}/read")
    public String read(@PathVariable Long id, Model model){
        CommunityResponseDto communityResponseDto = communityService.readById(id);
        model.addAttribute("community",communityResponseDto);
        return "community/read";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        communityService.delete(id, currentUser.getId());
        return "redirect:/communities/getAll";
    }

    @GetMapping("/{user_id}/getByUserId")
    public String getCommunitiesByUserId(@PathVariable("user_id") Long userId, Model model){
        List<CommunityResponseDto> communities = communityService.getByOwnerId(userId);
        model.addAttribute("communities", communities);
        return "user/communities";
    }

    @GetMapping("/getAll")
    public String getAll(Model model){
        List<CommunityResponseDto> communities = communityService.getAll();
        model.addAttribute("communities",communities);
        return "community/getAll";
    }

}
