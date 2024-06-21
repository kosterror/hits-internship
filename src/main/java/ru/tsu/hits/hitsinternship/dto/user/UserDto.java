package ru.tsu.hits.hitsinternship.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.group.GroupDto;
import ru.tsu.hits.hitsinternship.dto.practice.BasePracticeDto;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.entity.UserStatus;

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

    @Schema(description = "Статус пользователя", requiredMode = REQUIRED)
    private UserStatus status;

    @Schema(description = "Список ролей пользователя", requiredMode = REQUIRED)
    private List<Role> roles;

    @Schema(description = "Группа", requiredMode = NOT_REQUIRED)
    private GroupDto group;

    @Schema(description = "Текущее место практики", requiredMode = NOT_REQUIRED)
    private BasePracticeDto currentPractice;

}
