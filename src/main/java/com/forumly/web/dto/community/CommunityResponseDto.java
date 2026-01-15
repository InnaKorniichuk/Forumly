package com.forumly.web.dto.community;

import com.forumly.web.dto.post.PostDto;
import com.forumly.web.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.List;

@Value
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunityResponseDto {
    Long id;
    String name;
    LocalDateTime createdAt;
    UserDto owner;
    List<PostDto> posts;
}
