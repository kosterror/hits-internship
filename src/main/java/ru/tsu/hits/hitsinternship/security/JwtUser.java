package ru.tsu.hits.hitsinternship.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.hitsinternship.entity.Role;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class JwtUser {

    private UUID id;

    private String email;

    private List<Role> roles;

}
