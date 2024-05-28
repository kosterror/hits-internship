package ru.tsu.hits.hitsinternship.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

@Data
public class NewSolutionDto {

    @Schema(description = "Комментарий к решению", requiredMode = NOT_REQUIRED)
    private String comment;

    @Schema(description = "Список идентификаторов файлов", requiredMode = NOT_REQUIRED)
    private List<UUID> fileIds;

}
