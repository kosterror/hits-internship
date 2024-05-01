package ru.tsu.hits.hitsinternship.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    public JwtAuthenticationToken(JwtUser jwtUser) {
        super(jwtUser.getRoles()
                .stream()
                .map(el -> new SimpleGrantedAuthority("ROLE_" + el.toString()))
                .toList()
        );
        setDetails(jwtUser);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return super.getDetails();
    }
}
