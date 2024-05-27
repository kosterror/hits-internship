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
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.exception.UnauthorizedException;
import ru.tsu.hits.hitsinternship.security.JwtUser;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.jwt.access.expiration-min}")
    private int expiration;

    @Value("${application.jwt.secret}")
    private String secret;

    private final UserService userService;

    public String generateToken(UserEntity user) {
        return generateAccessToken(user);
    }

    private String generateAccessToken(UserEntity user) {
        Date issuedAt = new Date();
        long millis = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("roles", user.getRoles())
                .claim("email", user.getEmail())
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(millis + 1000L * 60 * expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtUser getAuthenticationToken(String jwtToken) {
        var userId = verifyTokenAndExtractUserId(jwtToken);
        var user = userService.getUserEntityById(userId);

        return new JwtUser(user.getId(),
                user.getEmail(),
                user.getRoles()
        );
    }

    private UUID verifyTokenAndExtractUserId(String token) {
        try {
            var key = getSignKey();
            Jws<Claims> data = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = data.getBody();
            return UUID.fromString(claims.getSubject());
        } catch (Exception exception) {
            throw new UnauthorizedException("Unauthorized", exception);
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
