package com.forumly.web.controller;

import com.forumly.web.dto.user.UserRequestDto;
import com.forumly.web.dto.user.UserResponseDto;
import com.forumly.web.dto.user.UserUpdateDto;
import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.User;
import com.forumly.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        return "create-user";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") UserRequestDto userDto,
                         BindingResult result,
                         Model model){
        if (result.hasErrors()) {
            return "create-user";
        }

        User newUser = userService.create(userDto);
        return "redirect:/users/" + newUser.getId() + "/read";
    }

    @GetMapping("/{id}/read")
    public String read(@PathVariable Long id, Model model){
        UserResponseDto userDto = userService.readById(id);
        model.addAttribute("user", userDto);
        return "user-info";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal User currentUser){
        if (!currentUser.getId().equals(id)) {
            throw ApplicationException.unauthorized("Cannot delete other users");
        }
        //TODO check for admin
        userService.delete(id);
        return "user-communities"; //TODO write list of communitites
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @AuthenticationPrincipal User currentUser,
                         Model model) {
        if (!currentUser.getId().equals(id)) {
            return "redirect:/users/" + currentUser.getId() + "/update";
        }

        UserResponseDto user = userService.readById(id);
        model.addAttribute("userInfo", user);
        model.addAttribute("userUpdate", new UserUpdateDto());
        return "update-user";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("userUpdate") UserUpdateDto dto,
                         @AuthenticationPrincipal User currentUser,
                         BindingResult result,
                         Model model) {
        if (!currentUser.getId().equals(id)) {
            throw ApplicationException.unauthorized("Cannot delete other users");
        }

        if (result.hasErrors()) {
            UserResponseDto userInfo = userService.readById(id);
            model.addAttribute("userInfo", userInfo);
            return "update-user";
        }

        userService.update(id, dto);
        return "redirect:/users/" + id + "/read";

    }

}
