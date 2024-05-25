package ru.tsu.hits.hitsinternship.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.tsu.hits.hitsinternship.service.JwtService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private static final String AUTHORIZATION_SCHEME_BEARER = "Bearer";
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationConverter.class);

    private final JwtService jwtService;

    @Override
    public Authentication convert(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return null;
        }

        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHORIZATION_SCHEME_BEARER)) {
            return null;
        }

        if (header.equalsIgnoreCase(AUTHORIZATION_SCHEME_BEARER)) {
            log.warn("Not found bearer authentication token");
            return null;
        }

        String token = header.substring(AUTHORIZATION_SCHEME_BEARER.length() + 1);

        try {
            JwtUser user = jwtService.getAuthenticationToken(token);
            return new JwtAuthentication(user, token);
        } catch (Exception exception) {
            log.error("Error parsing token", exception);
            return null;
        }
    }

}