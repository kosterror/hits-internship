package ru.tsu.hits.hitsinternship.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.hitsinternship.entity.PairNumber;

import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
public class MeetingsGroupedByNumber {

    @Schema(description = "Номер пары", requiredMode = REQUIRED)
    private PairNumber pairNumber;

    @Schema(description = "Список встреч на понедельник", requiredMode = REQUIRED)
    private Set<MeetingDto> mondayMeetings;

    @Schema(description = "Список встреч на вторник", requiredMode = REQUIRED)
    private Set<MeetingDto> tuesdayMeetings;

    @Schema(description = "Список встреч на среду", requiredMode = REQUIRED)
    private Set<MeetingDto> wednesdayMeetings;

    @Schema(description = "Список встреч на четверг", requiredMode = REQUIRED)
    private Set<MeetingDto> thursdayMeetings;

    @Schema(description = "Список встреч на пятницу", requiredMode = REQUIRED)
    private Set<MeetingDto> fridayMeetings;

    @Schema(description = "Список встреч на субботу", requiredMode = REQUIRED)
    private Set<MeetingDto> saturdayMeetings;

}
