package ru.tsu.hits.hitsinternship.dto.companyWishes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewCompanyWishDto {

    @NotBlank
    private String internAmount;

    private String comment;

    private UUID programLanguageId;
    @NotNull
    private UUID specialityId;
    @NotNull
    private UUID companyId;
}
