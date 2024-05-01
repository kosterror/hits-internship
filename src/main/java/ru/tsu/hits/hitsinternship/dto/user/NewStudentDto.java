package ru.tsu.hits.hitsinternship.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.tsu.hits.hitsinternship.util.ValidationRegexes;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class NewStudentDto {

    @Schema(description = "ФИО", requiredMode = REQUIRED)
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Schema(description = "ФИО", requiredMode = REQUIRED)
    @Pattern(regexp = ValidationRegexes.EMAIL_REGEX, message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

}
