package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.specialties.NewSpecialityDto;
import ru.tsu.hits.hitsinternship.dto.specialties.SpecialityDto;
import ru.tsu.hits.hitsinternship.service.SpecialityService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/specialties")
@RequiredArgsConstructor
@Tag(name = "Специальности")
public class SpecialityController {

    private final SpecialityService specialityService;

    @Operation(summary = "Создать специальность", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    public SpecialityDto createSpeciality(@Valid @RequestBody NewSpecialityDto newSpecialityDto) {
        return specialityService.createSpeciality(newSpecialityDto);
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить специальность", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{specialityId}")
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    public ResponseEntity<Void> deleteSpeciality(@Valid @PathVariable UUID specialityId) {
        specialityService.deleteSpeciality(specialityId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить список специальностей")
    @GetMapping
    public List<SpecialityDto> getSpecialities() {
        return specialityService.getSpecialities();
    }
}
