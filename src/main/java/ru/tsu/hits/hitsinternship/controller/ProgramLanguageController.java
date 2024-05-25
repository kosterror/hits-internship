package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.programlanguage.NewProgramLanguageDto;
import ru.tsu.hits.hitsinternship.dto.programlanguage.ProgramLanguageDto;
import ru.tsu.hits.hitsinternship.service.ProgramLanguageService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;


@RestController
@RequestMapping("/api/v1/program-languages")
@RequiredArgsConstructor
@Tag(name = "Языки программирования")
public class ProgramLanguageController {

    private final ProgramLanguageService programLanguageService;

    @Operation(summary = "Создать язык программирования", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public ProgramLanguageDto createProgramLanguage(@Valid @RequestBody NewProgramLanguageDto newProgramLanguageDto) {
        return programLanguageService.createProgramLanguage(newProgramLanguageDto);
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить язык программирования", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{programLanguageId}")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public ResponseEntity<Void> deleteProgramLanguage(@PathVariable UUID programLanguageId) {
        programLanguageService.deleteProgramLanguage(programLanguageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить список  языков программирования")
    @GetMapping
    public List<ProgramLanguageDto> getProgramLanguages() {
        return programLanguageService.getProgramLanguages();
    }
}
