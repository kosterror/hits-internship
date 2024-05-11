package ru.tsu.hits.hitsinternship.dto.companyWishes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewCompanyWishDto {

    private String internAmount;

    private String comment;

    private UUID programLanguageId;

    private UUID specialityId;

    private UUID companyId;
}
