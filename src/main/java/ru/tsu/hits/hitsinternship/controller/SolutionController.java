package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.NewSolutionDto;
import ru.tsu.hits.hitsinternship.dto.PaginationResponse;
import ru.tsu.hits.hitsinternship.dto.SolutionDto;
import ru.tsu.hits.hitsinternship.entity.SolutionState;
import ru.tsu.hits.hitsinternship.service.SolutionService;

import java.util.List;
import java.util.UUID;

import static ru.tsu.hits.hitsinternship.util.SecurityUtil.extractId;

@Tag(name = "Ответы на задания")
@RequestMapping("/api/v1/tasks/{taskId}/solutions")
@RestController
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @Operation(summary = "Добавить ответ на задание", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public SolutionDto createSolution(@PathVariable UUID taskId,
                                      @RequestBody @Valid NewSolutionDto newSolutionDto
    ) {
        return solutionService.createSolution(taskId, newSolutionDto, extractId());
    }

    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    @Operation(summary = "Получить ответы на задание", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public PaginationResponse<SolutionDto> getSolution(@PathVariable UUID taskId,
                                                       @RequestParam(required = false) String fullName,
                                                       @RequestParam(required = false) List<SolutionState> solutionStates,
                                                       @RequestParam(defaultValue = "0") int pageNumber,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        return solutionService.getTaskSolutions(taskId, fullName, solutionStates, pageNumber, pageSize);
    }

    @Operation(summary = "Получить мои ответы на задание", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/my")
    public List<SolutionDto> getMySolutions(@PathVariable UUID taskId) {
        return solutionService.getMySolutions(taskId, extractId());
    }

    @Operation(summary = "Изменить ответ на задание", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{solutionId}")
    public SolutionDto updateSolution(@PathVariable UUID taskId,
                                      @PathVariable UUID solutionId,
                                      @RequestBody @Valid NewSolutionDto newSolutionDto
    ) {
        return solutionService.updateSolution(taskId, solutionId, newSolutionDto, extractId());
    }

    @Operation(summary = "Принять ответ на задание", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/{solutionId}/accept")
    public SolutionDto acceptSolution(@PathVariable UUID taskId,
                                      @PathVariable UUID solutionId,
                                      @RequestParam int mark) {
        return solutionService.acceptSolution(taskId, solutionId, mark);
    }

    @Operation(summary = "Отклонить ответ на задание", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/{solutionId}/reject")
    public SolutionDto rejectSolution(@PathVariable UUID taskId,
                                      @PathVariable UUID solutionId) {
        return solutionService.rejectSolution(taskId, solutionId);
    }


}
