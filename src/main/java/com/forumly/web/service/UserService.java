package com.forumly.web.service;

import com.forumly.web.dto.user.UserRequestDto;
import com.forumly.web.dto.user.UserResponseDto;
import com.forumly.web.model.User;

import java.util.List;

public interface UserService {
    User create(UserRequestDto dto);

    UserResponseDto readById(Long id);

    User update(Long id, UserRequestDto dto);

    void delete(Long id);

    List<User> getAll();

    User findByEmail(String email);

    User getByNickname(String nickname);
}
