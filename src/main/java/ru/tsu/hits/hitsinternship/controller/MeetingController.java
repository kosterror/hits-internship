package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.meeting.MeetingDto;
import ru.tsu.hits.hitsinternship.dto.meeting.MeetingsGroupedByNumber;
import ru.tsu.hits.hitsinternship.dto.meeting.NewMeetingDto;
import ru.tsu.hits.hitsinternship.service.MeetingService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    @Operation(summary = "Создать встречу", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public MeetingDto createMeeting(@RequestBody @Valid NewMeetingDto newMeetingDto) {
        return meetingService.createMeeting(newMeetingDto);
    }

    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    @Operation(summary = "Изменить встречу", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public MeetingDto updateMeeting(@PathVariable UUID id,
                                    @RequestBody @Valid NewMeetingDto newMeetingDto) {
        return meetingService.updateMeeting(id, newMeetingDto);
    }

    @PreAuthorize("hasAnyRole('DEAN_OFFICER', 'CURATOR')")
    @Operation(summary = "Удалить встречу", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable UUID id) {
        meetingService.deleteMeeting(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить встречи")
    @GetMapping
    public List<MeetingsGroupedByNumber> getMeetings(@RequestParam List<UUID> groupIds,
                                                     @RequestParam LocalDate from,
                                                     @RequestParam LocalDate to) {
        return meetingService.getMeetings(groupIds, from, to);
    }

}
