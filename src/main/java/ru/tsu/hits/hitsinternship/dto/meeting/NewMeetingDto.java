package ru.tsu.hits.hitsinternship.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.tsu.hits.hitsinternship.entity.PairNumber;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class NewMeetingDto {

    @Schema(description = "Номер пары", requiredMode = REQUIRED)
    @NotNull(message = "Номер пары не может быть пустым")
    private PairNumber pairNumber;

    @Schema(description = "Дата", requiredMode = REQUIRED)
    @NotNull(message = "Дата не может быть пустой")
    private LocalDate date;

    @Schema(description = "Аудитория", requiredMode = REQUIRED)
    @NotNull(message = "Аудитория не может быть пустой")
    private String audience;

    @Schema(description = "Список идентификаторов групп", requiredMode = REQUIRED)
    @NotEmpty(message = "Список идентификаторов групп не может быть пустым")
    private List<UUID> groupIds;

    @Schema(description = "Идентификатор компании", requiredMode = REQUIRED)
    @NotNull
    private UUID companyId;

}
