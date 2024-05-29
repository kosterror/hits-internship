package ru.tsu.hits.hitsinternship.dto.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@Builder
public class FinalPositionDto {

    @Schema(description = "Студент", requiredMode = REQUIRED)
    private UserDto student;

    @Schema(description = "Позиции в конечном статус. Список может быть пустым",
            requiredMode = REQUIRED)
    private List<PositionDto> positions;

}
