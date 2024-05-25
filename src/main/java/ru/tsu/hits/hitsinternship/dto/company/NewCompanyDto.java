package ru.tsu.hits.hitsinternship.dto.company;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewCompanyDto {

    @NotNull
    private String name;

    private String websiteLink;

    @NotNull
    private Boolean isVisible;
}
