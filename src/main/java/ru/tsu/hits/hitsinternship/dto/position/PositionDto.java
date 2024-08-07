package ru.tsu.hits.hitsinternship.dto.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;
import ru.tsu.hits.hitsinternship.dto.programlanguage.ProgramLanguageDto;
import ru.tsu.hits.hitsinternship.dto.specialties.SpecialityDto;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;
import ru.tsu.hits.hitsinternship.entity.PositionStatus;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PositionDto {

    private UUID id;

    private PositionStatus positionStatus;

    private Integer priority;

    private ProgramLanguageDto programLanguage;

    private SpecialityDto speciality;

    private CompanyDto company;

    private UserDto user;
}
