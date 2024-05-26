package ru.tsu.hits.hitsinternship.dto.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewPracticeDto {

    private String comment;

    private UUID semesterId;

    private UUID userId;

    private UUID companyId;
}
