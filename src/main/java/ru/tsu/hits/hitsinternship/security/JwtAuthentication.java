package ru.tsu.hits.hitsinternship.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@EqualsAndHashCode(callSuper = true)
public class JwtAuthentication extends AbstractAuthenticationToken {

    private static final String ROLE_PREFIX = "ROLE_";

    private final transient JwtUser user;
    private final transient String token;

    public JwtAuthentication(JwtUser user, String token) {
        super(user.getRoles()
                .stream()
                .map(el -> new SimpleGrantedAuthority(ROLE_PREFIX + el.toString()))
                .toList()
        );
        super.setAuthenticated(true);
        this.user = user;
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
