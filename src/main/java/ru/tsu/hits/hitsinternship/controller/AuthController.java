package ru.tsu.hits.hitsinternship.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.hitsinternship.dto.user.NewStudentDto;
import ru.tsu.hits.hitsinternship.dto.user.NewUserDto;
import ru.tsu.hits.hitsinternship.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody NewUserDto newUserDto) {
        authService.register(newUserDto);
    }

    @PostMapping("/register-students")
    public void registerStudents(@Valid @RequestBody List<NewStudentDto> newStudentDtos) {
        authService.registerStudents(newStudentDtos);
    }

}
