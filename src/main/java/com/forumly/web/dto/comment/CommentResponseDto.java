package com.forumly.web.dto.comment;

import com.forumly.web.dto.post.PostDto;
import com.forumly.web.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponseDto {
    Long id;
    String content;
    int score;
    LocalDateTime createdAt;
    UserDto author;
    PostDto post;
}
