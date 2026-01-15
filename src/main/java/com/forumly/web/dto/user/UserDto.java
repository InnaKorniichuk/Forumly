package com.forumly.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;

    @NotBlank(message = "The 'nickname' cannot be empty")
    private String nickname;

    @NotBlank
    private String email;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof UserDto userDto)) return false;

        return id == userDto.id && Objects.equals(nickname, userDto.nickname) && Objects.equals(email, userDto.email);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + Objects.hashCode(nickname);
        result = 31 * result + Objects.hashCode(email);
        return result;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
