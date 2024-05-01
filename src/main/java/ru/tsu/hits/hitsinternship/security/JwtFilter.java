package ru.tsu.hits.hitsinternship.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tsu.hits.hitsinternship.dto.api.ApiError;
import ru.tsu.hits.hitsinternship.service.JwtService;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final EndpointsPermitAll endpointsPermitAll;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            String token = authorizationValue.substring(7);
            var authenticationToken = jwtService.getAuthenticationToken(token);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception exception) {
            log.error("Exception at jwt filter", exception);
            sendError(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        List<AntPathRequestMatcher> requestMatchers = endpointsPermitAll.getRequestMatchers();

        for (AntPathRequestMatcher requestMatcher : requestMatchers) {
            if (!request.getRequestURI().startsWith("/api") ||
                    requestMatcher.matcher(request).isMatch()) {
                return true;
            }
        }

        return false;
    }

    private void sendError(HttpServletResponse response) throws IOException {
        log.error("Token was missing or verification failed");
        ApiError apiError = new ApiError("Not authorized");
        String responseBody = objectMapper.writeValueAsString(apiError);

        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }

}
