package ru.tsu.hits.hitsinternship.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.hitsinternship.entity.PairNumber;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
public class MeetingsGroupedByNumber {

    @Schema(description = "Номер пары", requiredMode = REQUIRED)
    private PairNumber pairNumber;

    @Schema(description = "Список встреч на понедельник", requiredMode = REQUIRED)
    private List<MeetingDto> mondayMeetings;

    @Schema(description = "Список встреч на вторник", requiredMode = REQUIRED)
    private List<MeetingDto> tuesdayMeetings;

    @Schema(description = "Список встреч на среду", requiredMode = REQUIRED)
    private List<MeetingDto> wednesdayMeetings;

    @Schema(description = "Список встреч на четверг", requiredMode = REQUIRED)
    private List<MeetingDto> thursdayMeetings;

    @Schema(description = "Список встреч на пятницу", requiredMode = REQUIRED)
    private List<MeetingDto> fridayMeetings;

    @Schema(description = "Список встреч на субботу", requiredMode = REQUIRED)
    private List<MeetingDto> saturdayMeetings;

}
