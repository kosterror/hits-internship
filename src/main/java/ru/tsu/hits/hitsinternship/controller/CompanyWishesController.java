package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.companywishes.CompanyWishDto;
import ru.tsu.hits.hitsinternship.dto.companywishes.NewCompanyWishDto;
import ru.tsu.hits.hitsinternship.service.CompanyWishesService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/company-wishes")
@RequiredArgsConstructor
@Tag(name = "Пожелания компаний")
public class CompanyWishesController {

    private final CompanyWishesService companyWishesService;

    @Operation(summary = "Создать запись о пожелании компании", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'COMPANY_OFFICER', 'CURATOR')")
    public CompanyWishDto createGroup(@Valid @RequestBody NewCompanyWishDto newCompanyWishDto) {
        return companyWishesService.createCompanyWish(newCompanyWishDto);
    }

    @Operation(summary = "Изменить пожелание компании", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{companyWishId}")
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'COMPANY_OFFICER', 'CURATOR')")
    public CompanyWishDto updateGroup(@Valid @PathVariable UUID companyWishId, @RequestBody NewCompanyWishDto newCompanyWishDto) {
        return companyWishesService.updateCompanyWish(companyWishId, newCompanyWishDto);
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить пожелание компании", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{companyWishId}")
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'COMPANY_OFFICER', 'CURATOR')")
    public ResponseEntity<Void> deleteCompanyWish(@PathVariable UUID companyWishId) {
        companyWishesService.deleteCompanyWish(companyWishId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить пожелания компании", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'COMPANY_OFFICER', 'CURATOR')")
    public List<CompanyWishDto> getCompanyWish(@PathVariable UUID companyId) {
        return companyWishesService.getCompanyWishes(companyId);
    }
}
