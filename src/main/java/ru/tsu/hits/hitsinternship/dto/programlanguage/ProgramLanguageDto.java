package ru.tsu.hits.hitsinternship.dto.programlanguage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProgramLanguageDto {

    private UUID id;

    private String name;
}
