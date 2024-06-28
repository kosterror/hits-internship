package ru.tsu.hits.hitsinternship.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.filemetainfo.FileMetaInfoDto;
import ru.tsu.hits.hitsinternship.dto.semester.SemesterDto;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class MyTaskDto {

    @Schema(description = "Идентификатор задачи", requiredMode = REQUIRED)
    private UUID id;

    @Schema(description = "Название задачи", requiredMode = REQUIRED)
    private String name;

    @Schema(description = "Описание задачи", requiredMode = NOT_REQUIRED)
    private String description;

    @Schema(description = "Дата создания задачи", requiredMode = REQUIRED)
    private LocalDateTime createdAt;

    @Schema(description = "Дедлайн", requiredMode = REQUIRED)
    private LocalDateTime deadline;

    @Schema(description = "Файлы", requiredMode = NOT_REQUIRED)
    private List<FileMetaInfoDto> files;

    @Schema(description = "Автор задачи", requiredMode = REQUIRED)
    private UserDto author;

    @Schema(description = "Семестр", requiredMode = NOT_REQUIRED)
    private SemesterDto semester;

}
