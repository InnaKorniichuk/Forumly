package com.forumly.web.dto.post;

import lombok.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostRequestDto {
    String title;
}
