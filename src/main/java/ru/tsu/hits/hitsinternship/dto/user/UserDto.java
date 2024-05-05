package ru.tsu.hits.hitsinternship.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.tsu.hits.hitsinternship.entity.Role;

import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class UserDto {

    @Schema(description = "Идентификатор пользователя", requiredMode = REQUIRED)
    private UUID id;

    @Schema(description = "ФИО пользователя", requiredMode = REQUIRED)
    private String fullName;

    @Schema(description = "Email пользователя", requiredMode = REQUIRED)
    private String email;

    @Schema(description = "Статус пользователя (активирован ли он)", requiredMode = REQUIRED)
    private Boolean isActive;

    @Schema(description = "Список ролей пользователя", requiredMode = REQUIRED)
    private List<Role> roles;

    @Schema(description = "Идентификатор группы пользователя", requiredMode = NOT_REQUIRED)
    private UUID groupId;

}