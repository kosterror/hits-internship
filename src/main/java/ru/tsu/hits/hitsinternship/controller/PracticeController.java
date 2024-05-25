package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.practice.EditPracticeDto;
import ru.tsu.hits.hitsinternship.dto.practice.NewPracticeDto;
import ru.tsu.hits.hitsinternship.dto.practice.PracticeDto;
import ru.tsu.hits.hitsinternship.service.PracticeService;
import ru.tsu.hits.hitsinternship.util.SecurityUtil;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/practices")
@RequiredArgsConstructor
@Tag(name = "Места практики")
public class PracticeController {

    private final PracticeService practiceService;

    @Operation(summary = "Создать место практики", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public PracticeDto createPractice(@Valid @RequestBody NewPracticeDto newPracticeDto) {

        return practiceService.createPractice(newPracticeDto);
    }

    @Operation(summary = "Изменить место практики", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{practiceId}/change-priority")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public PracticeDto updatePractice(@Valid @PathVariable UUID practiceId, @RequestBody EditPracticeDto editPracticeDto) {
        return practiceService.updatePractice(practiceId, editPracticeDto);
    }

    @Operation(summary = "Получить места практики студента", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'STUDENT', 'CURATOR')")
    public List<PracticeDto> getStudentPractices(@PathVariable UUID userId) {
        return practiceService.getStudentPractices(SecurityUtil.extractId(), userId);
    }
}
