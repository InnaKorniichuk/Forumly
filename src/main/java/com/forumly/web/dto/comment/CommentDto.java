package com.forumly.web.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto {
    private Long id;

    @NotBlank
    private String content;

    private int score;

    @NotNull
    private LocalDateTime createdAt;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CommentDto that)) return false;

        return score == that.score && Objects.equals(id, that.id) && Objects.equals(content, that.content) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(content);
        result = 31 * result + score;
        result = 31 * result + Objects.hashCode(createdAt);
        return result;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", createdAt=" + createdAt +
                '}';
    }
}

