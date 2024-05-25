package ru.tsu.hits.hitsinternship.dto.programlanguage;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewProgramLanguageDto {

    @NotNull
    private String name;
}
