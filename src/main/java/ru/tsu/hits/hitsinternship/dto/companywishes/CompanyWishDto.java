package ru.tsu.hits.hitsinternship.dto.companyWishes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;
import ru.tsu.hits.hitsinternship.dto.programLanguage.ProgramLanguageDto;
import ru.tsu.hits.hitsinternship.dto.specialties.SpecialityDto;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompanyWishDto {

    private UUID id;

    private String internAmount;

    private String comment;

    private ProgramLanguageDto programLanguage;

    private SpecialityDto speciality;

    private CompanyDto company;
}
