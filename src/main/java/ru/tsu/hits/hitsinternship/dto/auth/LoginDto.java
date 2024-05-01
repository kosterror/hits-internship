package ru.tsu.hits.hitsinternship.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.tsu.hits.hitsinternship.util.ValidationRegexes;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class LoginDto {

    @Schema(description = "ФИО", requiredMode = REQUIRED, example = "DEAN_OFFICER")
    @Pattern(regexp = ValidationRegexes.EMAIL_REGEX, message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Пароль", requiredMode = REQUIRED, example = "password")
    @NotBlank
    private String password;

}
