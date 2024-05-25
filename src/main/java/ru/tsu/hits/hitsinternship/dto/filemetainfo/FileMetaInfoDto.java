package ru.tsu.hits.hitsinternship.dto.filemetainfo;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record FileMetaInfoDto(
        @Schema(description = "Идентификатор", requiredMode = REQUIRED)
        UUID id,

        @Schema(description = "Имя файла", requiredMode = REQUIRED)
        String name,

        @Schema(description = "Размер файла в MB", requiredMode = REQUIRED)
        double size,

        @Schema(description = "Идентификатор владельца файла", requiredMode = REQUIRED)
        UserDto owner,

        @Schema(description = "Дата и время загрузки файла", requiredMode = REQUIRED)
        LocalDateTime uploadDateTime
) {
}
