package ru.tsu.hits.hitsinternship.dto.practice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;

import java.util.List;

@Data
@AllArgsConstructor
public class PracticeMigrationResult {

    @Schema(description = "Список практик, перенесенных во время миграции")
    private List<PracticeDto> migratedPractices;

    @Schema(description = "Список практик, которые были созданы до миграции")
    private List<PracticeDto> existedPractices;

    @Schema(description = "Список пользователей, для которых не была найдена практика в предыдущем семестре, для переноса")
    private List<UserDto> practicesNotFound;

}
