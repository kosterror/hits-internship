package ru.tsu.hits.hitsinternship.dto.changepracticeapplication;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.hitsinternship.entity.ChangePracticeApplicationStatus;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewApplicationDto {

    private String comment;

    private String notPartner;

    private ChangePracticeApplicationStatus status;

    private UUID companyId;

    private UUID semesterId;
}
