package ru.tsu.hits.hitsinternship.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tsu.hits.hitsinternship.dto.auth.TokensDto;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.exception.UnauthorizedException;
import ru.tsu.hits.hitsinternship.security.JwtUser;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
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
                .setSubject(user.getId().toString())
                .claim("roles", user.getRoles())
                .claim("email", user.getEmail())
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(millis + 1000L * 60 * accessExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(UserEntity user, Date issuedAt, long millis) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(millis + 1000L * 60 * 60 * 24 * refreshExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtUser decodeAccessToken(String token) {
        try {
            var key = getSignKey();
            Jws<Claims> data = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = data.getBody();

            ArrayList<?> list = claims.get("roles", ArrayList.class);
            ArrayList<Role> roles = new ArrayList<>();

            for (Object el : list) {
                if (el instanceof String string) {
                    roles.add(Role.valueOf(string));
                }
            }

            return new JwtUser(
                    UUID.fromString(claims.getSubject()),
                    claims.get("email", String.class),
                    roles
            );
        } catch (Exception exception) {
            throw new UnauthorizedException("Unauthorized", exception);
        }
    }
}