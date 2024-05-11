package ru.tsu.hits.hitsinternship.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompanyDto {

    private UUID id;

    private String name;

    private String websiteLink;

    private Boolean isVisible;
}
