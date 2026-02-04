package com.forumly.web.controller;

import com.forumly.web.dto.community.CommunityRequestDto;
import com.forumly.web.dto.community.CommunityResponseDto;
import com.forumly.web.model.Community;
import com.forumly.web.model.User;
import com.forumly.web.service.CommunityService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/communities")
public class CommunityController {
    private final CommunityService communityService;

    public CommunityController(CommunityService communityService){
        this.communityService = communityService;
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("Community", new CommunityRequestDto());
        return "create-community";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("community") CommunityRequestDto communityDto,
                         @AuthenticationPrincipal User currentUser,
                         BindingResult result){
        if (result.hasErrors()) {
            return "create-community";
        }

        Community community = communityService.create(communityDto, currentUser.getId());
        return "redirect:/communities/" + community.getId() + "/read";
    }

    @GetMapping("/{id}/read")
    public String read(@PathVariable Long id, Model model){
        CommunityResponseDto communityResponseDto = communityService.readById(id);
        model.addAttribute("community",communityResponseDto);
        return "community-info";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id){
        //TODO only admin can delete
        communityService.delete(id);
        return "communities";
    }

    @GetMapping("/{user_id}/getByUserId")
    public String getCommunitiesByUserId(@PathVariable("user_id") Long userId, Model model){
        List<CommunityResponseDto> communities = communityService.getByOwnerId(userId);
        model.addAttribute("communities", communities);
        return "communities";
    }

    @GetMapping("/getAll")
    public String getAll(Model model){
        List<CommunityResponseDto> communities = communityService.getAll();
        model.addAttribute("communities",communities);
        return "communities";
    }

}
