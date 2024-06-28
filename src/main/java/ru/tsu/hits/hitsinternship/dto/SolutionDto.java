package ru.tsu.hits.hitsinternship.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.filemetainfo.FileMetaInfoDto;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;
import ru.tsu.hits.hitsinternship.entity.Mark;
import ru.tsu.hits.hitsinternship.entity.SolutionState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class SolutionDto {

    @Schema(description = "Идентификатор решения", requiredMode = REQUIRED)
    private UUID id;

    @Schema(description = "Комментарий к решению", requiredMode = NOT_REQUIRED)
    private String comment;

    @Schema(description = "Оценка решения", requiredMode = NOT_REQUIRED)
    private Mark mark;

    @Schema(description = "Состояние решения", requiredMode = REQUIRED)
    private SolutionState state;

    @Schema(description = "Автор решения", requiredMode = REQUIRED)
    private UserDto author;

    @Schema(description = "Дата и время загрузки последнего изменения решения студентом", requiredMode = REQUIRED)
    private LocalDateTime lastUpdateDateTime;

    @Schema(description = "Список файлов", requiredMode = NOT_REQUIRED)
    private List<FileMetaInfoDto> files;

}
