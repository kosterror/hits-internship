package ru.tsu.hits.hitsinternship.dto.position;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.hitsinternship.entity.PositionStatus;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewPositionDto {

    @NotNull
    private Integer priority;

    @NotNull
    private PositionStatus positionStatus;

    private UUID programLanguageId;

    @NotNull
    private UUID specialityId;

    @NotNull
    private UUID companyId;
}
