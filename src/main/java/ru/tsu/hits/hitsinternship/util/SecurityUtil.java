package ru.tsu.hits.hitsinternship.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.tsu.hits.hitsinternship.security.JwtUser;

import java.util.UUID;

@UtilityClass
public class SecurityUtil {

    public static UUID extractId() {
        JwtUser jwtUser = getJwtUser();
        return jwtUser.getId();
    }

    private static JwtUser getJwtUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtUser) authentication.getPrincipal();
    }

}
