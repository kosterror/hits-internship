package ru.tsu.hits.hitsinternship.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class NewTaskDto {

    @Schema(description = "Название задачи", requiredMode = REQUIRED)
    @NotBlank(message = "Название задачи не может быть пустым")
    private String name;

    @Schema(description = "Описание задачи", requiredMode = NOT_REQUIRED)
    private String description;

    @Schema(description = "Дедлайн", requiredMode = REQUIRED)
    @NotNull
    private LocalDateTime deadline;

    @Schema(description = "Идентификатор семестра", requiredMode = REQUIRED)
    @NotNull
    private UUID semesterId;

    @Schema(description = "Идентификаторы файлов", requiredMode = NOT_REQUIRED)
    private List<UUID> files;

}
