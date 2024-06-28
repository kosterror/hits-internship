package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.practice.*;
import ru.tsu.hits.hitsinternship.service.PracticeService;
import ru.tsu.hits.hitsinternship.util.SecurityUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Места практики")
public class PracticeController {

    private final PracticeService practiceService;

    @Operation(summary = "Создать место практики", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/practices")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public PracticeDto createPractice(@Valid @RequestBody NewPracticeDto newPracticeDto) {
        return practiceService.createPractice(newPracticeDto);
    }

    @Operation(summary = "Получить места практики для каждого из группы студентов в рамках семестра",
            security = @SecurityRequirement(name = "BearerAuth"))
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    @GetMapping("/practices/reports")
    public PracticeReportDto getPractices(@RequestParam UUID semesterId,
                                          @RequestParam List<UUID> groupIds
    ) {
        return practiceService.getPractices(semesterId, groupIds);
    }

    @Operation(summary = "Скачать отчет по практике", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/practices/reports-download")
    public ResponseEntity<Resource> downloadPracticeReport(@RequestParam UUID semesterId,
                                                           @RequestParam List<UUID> groupIds
    ) throws IOException {
        var file = practiceService.downloadPracticesAndMarks(semesterId, groupIds);

        var contentDisposition = ContentDisposition.builder("file")
                .filename("отчет.xlsx", StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(file));
    }

    @Operation(summary = "Получить места практики студента", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/users/{userId}/practices")
    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'STUDENT', 'CURATOR')")
    public List<PracticeDto> getStudentPractices(@PathVariable UUID userId) {
        return practiceService.getStudentPractices(SecurityUtil.extractId(), userId);
    }

    @Operation(summary = "Перенести места практики из одного семестра в другой",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    @PostMapping("/practices/migrate")
    public PracticeMigrationResult migratePractices(@RequestParam UUID fromSemesterId,
                                                    @RequestParam UUID toSemesterId,
                                                    @RequestParam List<UUID> groupIds) {
        return practiceService.migratePractices(fromSemesterId, toSemesterId, groupIds);
    }

    @Operation(summary = "Изменить место практики", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/practices/{practiceId}")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public PracticeDto updatePractice(@Valid @PathVariable UUID practiceId,
                                      @RequestBody EditPracticeDto editPracticeDto) {
        return practiceService.updatePractice(practiceId, editPracticeDto);
    }

}
