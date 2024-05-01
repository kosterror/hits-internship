package ru.tsu.hits.hitsinternship.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tsu.hits.hitsinternship.dto.auth.TokensDto;
import ru.tsu.hits.hitsinternship.entity.UserEntity;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {

    @Value("${application.jwt.access.expiration-min}")
    private int accessExpiration;

    @Value("${application.jwt.refresh.expiration-days}")
    private int refreshExpiration;

    @Value("${application.jwt.secret}")
    private String secret;

    public TokensDto generateTokens(UserEntity user) {
        Date issuedAt = new Date();
        long millis = System.currentTimeMillis();

        String accessToken = generateAccessToken(user, issuedAt, millis);
        String refreshToken = generateRefreshToken(user, issuedAt, millis);

        return new TokensDto(accessToken, refreshToken);
    }

    private String generateAccessToken(UserEntity user, Date issuedAt, long millis) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", user.getRoles())
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(millis + 1000L * 60 * accessExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(UserEntity user, Date issuedAt, long millis) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(millis + 1000L * 60 * 60 * 24 * refreshExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
