package ru.tsu.hits.hitsinternship.dto.changepracticeapplication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;
import ru.tsu.hits.hitsinternship.dto.semester.SemesterDto;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;
import ru.tsu.hits.hitsinternship.entity.ChangePracticeApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChangePracticeApplicationDto {

    private UUID id;

    private String comment;

    private String notPartner;

    private ChangePracticeApplicationStatus status;

    private CompanyDto partner;

    private UserDto checkingEmployee;

    private UserDto author;

    private SemesterDto semester;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdatedDate;
}
