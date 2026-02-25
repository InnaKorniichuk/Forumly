package com.forumly.web.controller;

import com.forumly.web.dto.post.PostMapper;
import com.forumly.web.dto.post.PostResponseDto;
import com.forumly.web.dto.user.UserMapper;
import com.forumly.web.dto.user.UserRequestDto;
import com.forumly.web.dto.user.UserResponseDto;
import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.Role;
import com.forumly.web.model.User;
import com.forumly.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("user", new UserRequestDto());
        return "user/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") UserRequestDto userDto,
                         BindingResult result){
        if (result.hasErrors()) {
            return "user/create";
        }

        User newUser = userService.create(userDto);
        return "redirect:/users/" + newUser.getId() + "/read";
    }

    @GetMapping("/{id}/read")
    public String read(@PathVariable Long id,
                       Model model,
                       Principal principal){
        String email = principal.getName();
        User currentUser = userService.findByEmail(email);

        if(!currentUser.getRole().equals(Role.ADMIN)
                && !currentUser.getId().equals(id))
            throw ApplicationException.unauthorized("You are not allowed to view this user's profile ");

        UserResponseDto userDto = userService.readById(id);
        List<PostResponseDto> posts = userService.findByEmail(userDto.getEmail())
                        .getPosts().stream()
                        .map(PostMapper::toResponse)
                        .toList();
        model.addAttribute("user", userDto);
        model.addAttribute("posts", posts);
        return "user/read";
    }

    @GetMapping("/all")
    public String getAll(Model model){
        List<UserResponseDto> users = userService.getAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
        model.addAttribute("users", users);
        return "user/all";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users/all";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         Principal principal,
                         Model model) {
        String email = principal.getName();
        User currentUser = userService.findByEmail(email);

        if(!currentUser.getId().equals(id))
            throw ApplicationException.unauthorized("You are not allowed to edit this user's profile ");

        UserResponseDto user = userService.readById(id);
        model.addAttribute("userInfo", user);
        model.addAttribute("userUpdate", new UserRequestDto());
        return "user/update";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("userUpdate") UserRequestDto dto,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            UserResponseDto userInfo = userService.readById(id);
            model.addAttribute("userInfo", userInfo);
            return "user/update";
        }

        userService.update(id, dto);
        return "redirect:/users/" + id + "/read";

    }

}
