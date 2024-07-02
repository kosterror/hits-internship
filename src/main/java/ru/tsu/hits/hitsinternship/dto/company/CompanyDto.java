package ru.tsu.hits.hitsinternship.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;

import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompanyDto {

    @Schema(description = "Идентификатор компании", requiredMode = REQUIRED)
    private UUID id;

    @Schema(description = "Название компании", requiredMode = REQUIRED)
    private String name;

    @Schema(description = "Ссылка на сайт компании", requiredMode = NOT_REQUIRED)
    private String websiteLink;

    @Schema(description = "Видимость компании", requiredMode = NOT_REQUIRED)
    private Boolean isVisible;

    @Schema(description = "Куратор компании", requiredMode = NOT_REQUIRED)
    private UserDto curator;

    @Schema(description = "Представитель компании компании", requiredMode = NOT_REQUIRED)
    private UserDto officer;
}
