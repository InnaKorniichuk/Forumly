package com.forumly.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateDto {

    @Nullable
    @Size(min = 3, max = 50, message = "Nickname must be between 3 and 50 characters")
    @JsonProperty("nickname")
    String nickname;

    @Nullable
    @Email(message = "Must be a valid email address")
    @Pattern(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Must be a valid email address")
    @JsonProperty("email")
    String email;

    @Nullable
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain upper, lower case letters and a digit"
    )
    @JsonProperty("password")
    String password;

    public boolean hasNicknameUpdate() {
        return nickname != null && !nickname.isBlank();
    }

    public boolean hasEmailUpdate() {
        return email != null && !email.isBlank();
    }

    public boolean hasPasswordUpdate() {
        return password != null && !password.isBlank();
    }

    public boolean hasUpdates() {
        return hasNicknameUpdate() || hasEmailUpdate() || hasPasswordUpdate();
    }
}
