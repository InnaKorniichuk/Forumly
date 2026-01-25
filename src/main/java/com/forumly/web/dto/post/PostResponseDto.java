package com.forumly.web.dto.post;

import com.forumly.web.dto.community.CommunityDto;
import com.forumly.web.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostResponseDto {
    Long id;
    String title;
    String content;
    LocalDateTime createdAt;
    int score;
    UserDto author;
    CommunityDto community;
}
