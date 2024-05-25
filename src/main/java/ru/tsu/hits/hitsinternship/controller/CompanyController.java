package ru.tsu.hits.hitsinternship.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;
import ru.tsu.hits.hitsinternship.dto.company.NewCompanyDto;
import ru.tsu.hits.hitsinternship.service.CompanyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
@Tag(name = "Компании")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "Создать компанию", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    public CompanyDto createCompany(@Valid @RequestBody NewCompanyDto newCompanyDto) {
        return companyService.createCompany(newCompanyDto);
    }

    @Operation(summary = "Удалить компанию", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    public void deleteCompany(@PathVariable UUID companyId) {
        companyService.deleteCompany(companyId);
    }

    @Operation(summary = "Обновить компанию", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    public CompanyDto updateCompany(@PathVariable UUID companyId, @Valid @RequestBody NewCompanyDto newCompanyDto) {
        return companyService.updateCompany(companyId, newCompanyDto);
    }

    @Operation(summary = "Получить список компаний")
    @GetMapping
    public List<CompanyDto> getCompanies() {
        return companyService.getCompanies();
    }
}