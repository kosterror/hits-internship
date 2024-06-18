package ru.tsu.hits.hitsinternship.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateStudentsRequest {

    @NotNull
    private UUID groupId;

    @NotEmpty
    private List<@Valid NewStudentDto> students;

}
