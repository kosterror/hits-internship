package ru.tsu.hits.hitsinternship.dto.specialties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewSpecialityDto {

    @NotNull
    @NotBlank
    private String name;
}
