package com.forumly.web.dto.user;

import lombok.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponseDto {
    Long id;
    String email;
    String nickname;
}
