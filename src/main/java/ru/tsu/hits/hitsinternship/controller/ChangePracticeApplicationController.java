package ru.tsu.hits.hitsinternship.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.hitsinternship.service.ChangePracticeApplicationService;

@RestController
@RequestMapping("/api/v1/change-practice-applications")
@RequiredArgsConstructor
@Tag(name = "Заявки на изменение места практики")
public class ChangePracticeApplicationController {

    private final ChangePracticeApplicationService changePracticeApplicationService;

//    @Operation(summary = "Создать заявку на изменение места практики", security = @SecurityRequirement(name = "BearerAuth"))
//    @PostMapping
//    @PreAuthorize("hasRole('STUDENT')")
//    public ChangePracticeApplicationDto createChangePracticeApplication(@Valid @RequestBody NewApplicationDto newApplicationDto) {
//
//        return changePracticeApplicationService.createChangePracticeApplication(newApplicationDto, SecurityUtil.extractId());
//    }
//
//    @Operation(summary = "Взять заявку в работу", security = @SecurityRequirement(name = "BearerAuth"))
//    @PostMapping("/{positionId}/take-to-work")
//    @PreAuthorize("hasRole('DEAN_OFFICER')")
//    public ChangePracticeApplicationDto takeApplicationToWork(@PathVariable UUID positionId) {
//        return changePracticeApplicationService.takeApplicationToWork(positionId, SecurityUtil.extractId());
//    }
//    @Operation(summary = "Удалить заявку на изменение места практики", security = @SecurityRequirement(name = "BearerAuth"))
//    @DeleteMapping("/{positionId}")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ChangePracticeApplicationDto deleteChangePracticeApplication(@PathVariable UUID positionId) {
//        return changePracticeApplicationService.deleteChangePracticeApplication(positionId, SecurityUtil.extractId());
//    }
//
//    @Operation(summary = "Принять решение по заявке на изменение места практики", security = @SecurityRequirement(name = "BearerAuth"))
//    @PostMapping("/{positionId}/make-decision")
//    @PreAuthorize("hasRole('DEAN_OFFICER')")
//    public ChangePracticeApplicationDto makeDecisionOnApplication(@PathVariable UUID positionId) {
//        return changePracticeApplicationService.makeDecisionOnApplication(positionId, SecurityUtil.extractId());
//    }
//
//    @Operation(summary = "Получить заявки на изменение мест практики", security = @SecurityRequirement(name = "BearerAuth"))
//    @GetMapping
//    @PreAuthorize("hasRole('DEAN_OFFICER')")
//    public ChangePracticeApplicationDto getChangePracticeApplications() {
//        return changePracticeApplicationService.getChangePracticeApplications();
//    }

}
