package ru.tsu.hits.hitsinternship.dto.practice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;

import java.util.List;

@Data
@AllArgsConstructor
public class PracticeReportDto {

    @Schema(description = "Список мест практик. На уровне БД в рамках одного семестра у одного пользователя не " +
            "может быть больше одной практики")
    private List<PracticeDto> practices;

    @Schema(description = "Список студентов без мест практик")
    private List<UserDto> studentsWithoutPractice;

}
