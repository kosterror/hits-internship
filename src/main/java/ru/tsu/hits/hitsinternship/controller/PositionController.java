package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.PaginationResponse;
import ru.tsu.hits.hitsinternship.dto.position.NewPositionDto;
import ru.tsu.hits.hitsinternship.dto.position.PositionDto;
import ru.tsu.hits.hitsinternship.entity.PositionStatus;
import ru.tsu.hits.hitsinternship.service.PositionService;
import ru.tsu.hits.hitsinternship.util.SecurityUtil;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@Tag(name = "Позиции")
public class PositionController {

    private final PositionService positionService;

    @Operation(summary = "Создать запись о позиции студента", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public PositionDto createPosition(@Valid @RequestBody NewPositionDto newPositionDto) {
        return positionService.createPosition(newPositionDto, SecurityUtil.extractId());
    }

    @Operation(summary = "Изменить статус позиции", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{positionId}/change-status")
    @PreAuthorize("hasRole('STUDENT')")
    public PositionDto updatePositionStatus(@Valid @PathVariable UUID positionId, @RequestBody PositionStatus positionStatus) {
        return positionService.updatePositionStatus(positionId, positionStatus, SecurityUtil.extractId());
    }

    @Operation(summary = "Изменить приоритет позиции", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/change-priority")
    @PreAuthorize("hasRole('STUDENT')")
    public List<PositionDto> updatePositionPriority(@RequestBody List<UUID> positionIdList) {
        return positionService.updatePositionPriority(positionIdList, SecurityUtil.extractId());
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить позицию", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{positionId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> deletePosition(@PathVariable UUID positionId) {
        positionService.deletePosition(positionId, SecurityUtil.extractId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить позиции студента", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'DEAN_OFFICER', 'CURATOR')")
    public List<PositionDto> getStudentPositions(@PathVariable UUID userId) {
        return positionService.getStudentPositions(userId, SecurityUtil.extractId());
    }


    @Operation(summary = "Подтвердить получение оффера", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/{positionId}/confirm-received-offer")
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    public PositionDto confirmReceivedOffer(@PathVariable UUID positionId) {
        return positionService.confirmReceivedOffer(positionId);
    }

    @Operation(summary = "Получить позиции", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    public PaginationResponse<PositionDto> getPositions(@RequestParam(required = false) List<UUID> companyIds,
                                                        @RequestParam(required = false) List<UUID> specialityIds,
                                                        @RequestParam(required = false) List<UUID> programLanguageIds,
                                                        @RequestParam(required = false) List<UUID> studentIds,
                                                        @RequestParam(required = false) List<UUID> groupIds,
                                                        @RequestParam(required = false) PositionStatus positionStatus,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return positionService.getPositions(companyIds,
                specialityIds,
                programLanguageIds,
                studentIds,
                groupIds,
                positionStatus,
                page,
                size);
    }

}
