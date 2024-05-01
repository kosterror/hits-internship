package ru.tsu.hits.hitsinternship.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Endpoint {
    private String pattern;
    private String method;
}
