package ru.tsu.hits.hitsinternship.dto.practice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
public class PracticeReportDto {

    @Schema(description = "Список мест практик. На уровне БД в рамках одного семестра у одного пользователя не " +
            "может быть больше одной практики",
            requiredMode = REQUIRED
    )
    private List<PracticeDto> practices;

    @Schema(description = "Список студентов без мест практик. Не может быть null, но может быть пустым",
            requiredMode = REQUIRED
    )
    private List<UserDto> studentsWithoutPractice;

}
