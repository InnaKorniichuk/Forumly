package com.forumly.web.dto.user;

import com.forumly.web.model.User;
import lombok.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRequestDto {
    String email;
    String password;
    String nickname;
}
