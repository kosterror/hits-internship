package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.semester.NewSemesterDto;
import ru.tsu.hits.hitsinternship.dto.semester.SemesterDto;
import ru.tsu.hits.hitsinternship.service.SemesterService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/semesters")
@RequiredArgsConstructor
@Tag(name = "Семестры")
public class SemesterController {

    private final SemesterService semesterService;

    @PreAuthorize("hasRole('DEAN_OFFICER')")
    @Operation(summary = "Создать семестр", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public SemesterDto createSemester(@RequestBody @Valid NewSemesterDto newSemesterDto) {
        return semesterService.createSemester(newSemesterDto);
    }

    @Operation(summary = "Получить семестр по id", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public SemesterDto getSemester(@PathVariable UUID id) {
        return semesterService.getSemester(id);
    }

    @Operation(summary = "Получить все семестры", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public List<SemesterDto> getSemesters() {
        return semesterService.getSemesters();
    }

    @PreAuthorize("hasRole('DEAN_OFFICER')")
    @Operation(summary = "Удалить семестр", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable UUID id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.noContent().build();
    }

}
