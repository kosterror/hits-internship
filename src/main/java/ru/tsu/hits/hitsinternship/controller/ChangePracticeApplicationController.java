package ru.tsu.hits.hitsinternship.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.changepracticeapplication.ChangePracticeApplicationDto;
import ru.tsu.hits.hitsinternship.dto.changepracticeapplication.NewApplicationDto;
import ru.tsu.hits.hitsinternship.entity.ChangePracticeApplicationStatus;
import ru.tsu.hits.hitsinternship.service.ChangePracticeApplicationService;
import ru.tsu.hits.hitsinternship.util.SecurityUtil;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/change-practice-applications")
@RequiredArgsConstructor
@Tag(name = "Заявки на изменение места практики")
public class ChangePracticeApplicationController {

    private final ChangePracticeApplicationService changePracticeApplicationService;

    @Operation(summary = "Создать заявку на изменение места практики",
            security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ChangePracticeApplicationDto createChangePracticeApplication(
            @Valid @RequestBody NewApplicationDto newApplicationDto
    ) {

        return changePracticeApplicationService.createChangePracticeApplication(newApplicationDto,
                SecurityUtil.extractId());
    }

    @Operation(summary = "Удалить заявку на изменение места практики",
            security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{applicationId}")
    @PreAuthorize("hasRole('STUDENT')")
    public void deleteChangePracticeApplication(@PathVariable UUID applicationId) {
        changePracticeApplicationService.deleteChangePracticeApplication(applicationId, SecurityUtil.extractId());
    }

    @Operation(summary = "Принять решение по заявке на изменение места практики",
            security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/{applicationId}/make-decision")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public ChangePracticeApplicationDto makeDecisionOnApplication(
            @PathVariable UUID applicationId,
            ChangePracticeApplicationStatus changePracticeApplicationStatus) {
        return changePracticeApplicationService.makeDecisionOnApplication(
                applicationId,
                SecurityUtil.extractId(),
                changePracticeApplicationStatus);
    }

    @Operation(summary = "Получить заявки на изменение мест практики",
            security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public List<ChangePracticeApplicationDto> getChangePracticeApplications() {
        return changePracticeApplicationService.getChangePracticeApplications();
    }

    @Operation(summary = "Получить свои заявки на изменение мест практики",
            security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public List<ChangePracticeApplicationDto> getStudentChangePracticeApplications() {
        return changePracticeApplicationService.getStudentChangePracticeApplications(SecurityUtil.extractId());
    }

}
