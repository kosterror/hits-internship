package ru.tsu.hits.hitsinternship.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;
import ru.tsu.hits.hitsinternship.dto.group.GroupDto;
import ru.tsu.hits.hitsinternship.entity.PairNumber;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class MeetingDto {

    @Schema(description = "Идентификатор", requiredMode = REQUIRED)
    private UUID id;

    @Schema(description = "Номер пары", requiredMode = REQUIRED)
    private PairNumber pairNumber;

    @Schema(description = "День недели", requiredMode = REQUIRED)
    private DayOfWeek dayOfWeek;

    @Schema(description = "Дата", requiredMode = REQUIRED)
    private LocalDate date;

    @Schema(description = "Аудитория", requiredMode = REQUIRED)
    private String audience;

    @Schema(description = "Список групп", requiredMode = REQUIRED)
    private List<GroupDto> groups;

    @Schema(description = "Компания", requiredMode = REQUIRED)
    private CompanyDto company;

}
