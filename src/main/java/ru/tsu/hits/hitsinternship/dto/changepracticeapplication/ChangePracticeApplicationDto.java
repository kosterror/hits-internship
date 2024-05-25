package ru.tsu.hits.hitsinternship.dto.changepracticeapplication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.hitsinternship.entity.ChangePracticeApplicationStatus;
import ru.tsu.hits.hitsinternship.entity.CompanyEntity;
import ru.tsu.hits.hitsinternship.entity.SemesterEntity;
import ru.tsu.hits.hitsinternship.entity.UserEntity;

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

    private CompanyEntity partner;

    private UserEntity checkingEmployee;

    private UserEntity author;

    private SemesterEntity semester;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdatedDate;
}
