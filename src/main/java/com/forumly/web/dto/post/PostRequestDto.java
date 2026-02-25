package com.forumly.web.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostRequestDto {
    @NotBlank
    @Size(min = 5, max = 200)
    String title;

    @NotBlank
    @Size(min = 3, max = 5000)
    String content;
}
