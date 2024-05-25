package ru.tsu.hits.hitsinternship.dto.practice;

import lombok.Data;

import java.util.UUID;

@Data
public class EditPracticeDto {

    private String comment;

    private UUID companyId;
}
