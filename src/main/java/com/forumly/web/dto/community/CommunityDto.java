package com.forumly.web.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDto {
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate createdAt;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CommunityDto that)) return false;

        return Objects.equals(id, that.id) && name.equals(that.name) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + name.hashCode();
        result = 31 * result + Objects.hashCode(createdAt);
        return result;
    }

    @Override
    public String toString() {
        return "CommunityDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
