package com.forumly.web.dto.community;

import lombok.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.PropertyNamingStrategy;
import tools.jackson.databind.annotation.JsonNaming;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunityRequestDto {
    String name;
}
