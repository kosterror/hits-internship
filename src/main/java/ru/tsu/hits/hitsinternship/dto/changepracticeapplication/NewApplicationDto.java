package ru.tsu.hits.hitsinternship.dto.changepracticeapplication;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewApplicationDto {

    private String comment;

    private String notPartner;

    private UUID companyId;

    @NotNull
    private UUID semesterId;
}
