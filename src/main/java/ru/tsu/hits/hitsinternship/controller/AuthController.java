package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.hitsinternship.dto.auth.LoginDto;
import ru.tsu.hits.hitsinternship.dto.auth.TokensDto;
import ru.tsu.hits.hitsinternship.dto.user.NewStudentDto;
import ru.tsu.hits.hitsinternship.dto.user.NewUserDto;
import ru.tsu.hits.hitsinternship.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Регистраци, авторизация")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Регистрация пользователя", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/register")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public void register(@Valid @RequestBody NewUserDto newUserDto) {
        authService.register(newUserDto);
    }

    @Operation(summary = "Регистрация студентов", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/register-students")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public void registerStudents(@Valid @RequestBody List<NewStudentDto> newStudentDtos) {
        authService.registerStudents(newStudentDtos);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public TokensDto login(@Valid @RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

}
