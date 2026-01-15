package com.forumly.web.dto.user;

import com.forumly.web.model.User;

public class UserMapper {
    public static User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        return user;
    }

    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }
}
