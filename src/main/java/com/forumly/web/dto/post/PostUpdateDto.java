package com.forumly.web.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostUpdateDto {
    @Nullable
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @JsonProperty("title")
    String title;

    @Nullable
    @Size(min = 3, max = 5000, message = "Content must be between 10 and 5000 characters")
    @JsonProperty("content")
    String content;

    public boolean hasUpdates() {
        return (title != null && !title.isBlank()) ||
                (content != null && !content.isBlank());
    }
}
