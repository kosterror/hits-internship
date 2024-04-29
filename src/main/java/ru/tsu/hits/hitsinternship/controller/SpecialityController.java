package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.specialties.NewSpecialityDto;
import ru.tsu.hits.hitsinternship.dto.specialties.SpecialityDto;
import ru.tsu.hits.hitsinternship.service.SpecialityService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/specialties")
@RequiredArgsConstructor
@Tag(name = "Специальности")
public class SpecialityController {

    private final SpecialityService specialityService;

    @Operation(summary = "Создать специальность")
    @PostMapping("")
    SpecialityDto createSpeciality(@Valid @RequestBody NewSpecialityDto newSpecialityDto) {

        return specialityService.createSpeciality(newSpecialityDto);
    }

    @Operation(summary = "Удалить специальность")
    @DeleteMapping("/{specialityId}")
    void deleteSpeciality(@Valid @PathVariable UUID specialityId) {

        specialityService.deleteSpeciality(specialityId);
    }

    @Operation(summary = "Получить список специальностей")
    @GetMapping("")
    List<SpecialityDto> getSpecialities() {
        return specialityService.getSpecialities();
    }
}
