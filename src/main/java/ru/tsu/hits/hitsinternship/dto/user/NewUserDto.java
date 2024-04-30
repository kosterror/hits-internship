package ru.tsu.hits.hitsinternship.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.tsu.hits.hitsinternship.entity.Role;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class NewUserDto {

    @Schema(description = "ФИО", requiredMode = REQUIRED)
    @NotBlank
    private String fullName;

    @Schema(description = "Почта", requiredMode = REQUIRED)
    @NotBlank
    private String email;

    @Schema(description = "Роль", requiredMode = REQUIRED)
    @NotBlank
    private Role role;

}
