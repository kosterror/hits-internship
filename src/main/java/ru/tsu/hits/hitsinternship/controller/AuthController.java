package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.auth.LoginDto;
import ru.tsu.hits.hitsinternship.dto.auth.TokensDto;
import ru.tsu.hits.hitsinternship.dto.user.CreateStudentsRequest;
import ru.tsu.hits.hitsinternship.dto.user.NewUserDto;
import ru.tsu.hits.hitsinternship.service.AuthService;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Регистрация, авторизация")
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Регистрация пользователя", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/register")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public ResponseEntity<Void> register(@Valid @RequestBody NewUserDto newUserDto) {
        authService.register(newUserDto);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Регистрация студентов", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/register-students")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public ResponseEntity<Void> registerStudents(@Valid @RequestBody CreateStudentsRequest createStudentsRequest) {
        authService.registerStudents(createStudentsRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public TokensDto login(@Valid @RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

}
