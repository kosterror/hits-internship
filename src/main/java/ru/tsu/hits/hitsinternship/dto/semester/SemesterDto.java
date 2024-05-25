package ru.tsu.hits.hitsinternship.dto.semester;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SemesterDto {

    @Schema(description = "Идентификатор семестра")
    private UUID id;

    @Schema(description = "Номер семестра")
    private int number;

    @Schema(description = "Учебный год")
    private String studyYear;

    @Schema(description = "Дата начала семестра")
    private LocalDate startDate;

    @Schema(description = "Дата окончания семестра")
    private LocalDate endDate;

    @Schema(description = "Дата окончания срока подачи заявок на смену компании")
    private LocalDateTime changeCompanyApplicationDeadline;

}
