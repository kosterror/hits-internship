package ru.tsu.hits.hitsinternship.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.practice.PracticeDto;
import ru.tsu.hits.hitsinternship.entity.Mark;

import java.util.List;

@Data
@AllArgsConstructor
public class SemesterReport {

    private UserDto user;

    private PracticeDto practice;

    private List<Mark> marks;

}
