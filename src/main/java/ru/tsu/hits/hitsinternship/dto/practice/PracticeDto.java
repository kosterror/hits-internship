package ru.tsu.hits.hitsinternship.dto.practice;


import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;
import ru.tsu.hits.hitsinternship.dto.semester.SemesterDto;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;

import java.util.UUID;

@Data
public class PracticeDto {

    private UUID id;

    private String comment;

    private SemesterDto semester;

    private UserDto user;

    private CompanyDto company;
}
