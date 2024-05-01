package ru.tsu.hits.hitsinternship.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TokensDto {

    private String accessToken;

    private String refreshToken;

}
