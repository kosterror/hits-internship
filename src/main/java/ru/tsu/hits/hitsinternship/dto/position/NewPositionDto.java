package ru.tsu.hits.hitsinternship.dto.position;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewPositionDto {

    private UUID id;

    private Integer priority;

    private UUID programLanguageId;

    private UUID specialityId;

    private UUID companyId;

    private UUID userId;
}
