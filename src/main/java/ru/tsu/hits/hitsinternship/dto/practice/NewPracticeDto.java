package ru.tsu.hits.hitsinternship.dto.practice;

import lombok.Data;

import java.util.UUID;

@Data
public class NewPracticeDto {

    private String comment;

    private UUID semesterId;

    private UUID userId;

    private UUID companyId;
}
