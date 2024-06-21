package ru.tsu.hits.hitsinternship.dto.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BasePracticeDto {

    private UUID id;

    private String comment;

    private CompanyDto company;

}
