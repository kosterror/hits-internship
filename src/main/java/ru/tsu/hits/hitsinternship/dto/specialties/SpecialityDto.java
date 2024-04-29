package ru.tsu.hits.hitsinternship.dto.specialties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class SpecialityDto {

    private UUID id;

    private String name;
}
