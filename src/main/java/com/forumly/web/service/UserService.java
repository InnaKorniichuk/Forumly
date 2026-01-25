package com.forumly.web.service;

import com.forumly.web.dto.user.UserRequestDto;
import com.forumly.web.dto.user.UserUpdateDto;
import com.forumly.web.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User create(UserRequestDto dto);

    User readById(Long id);

    User update(Long id, UserUpdateDto dto);

    void delete(Long id);

    List<User> getAll();

    User getByEmail(String email);

    User getByNickname(String nickname);
}
