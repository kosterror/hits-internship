package ru.tsu.hits.hitsinternship.dto.semester;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class NewSemesterDto {

    @Schema(description = "Номер семестра", requiredMode = REQUIRED)
    @NotNull(message = "Номер семестра не может быть пустым")
    private int number;

    @Schema(description = "Учебный год", requiredMode = REQUIRED)
    @NotNull(message = "Учебный год не может быть пустым")
    private String studyYear;

    @Schema(description = "Дата начала семестра", requiredMode = REQUIRED)
    @NotNull(message = "Дата начала семестра не может быть пустой")
    private LocalDate startDate;

    @Schema(description = "Дата окончания семестра", requiredMode = REQUIRED)
    @NotNull(message = "Дата окончания семестра не может быть пустой")
    private LocalDate endDate;

    @Schema(description = "Дата окончания срока подачи заявок на смену компании", requiredMode = REQUIRED)
    @NotNull(message = "Дата окончания срока подачи заявок на смену компании не может быть пустой")
    private LocalDateTime changeCompanyApplicationDeadline;

}
