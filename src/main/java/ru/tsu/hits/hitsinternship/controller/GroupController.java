package ru.tsu.hits.hitsinternship.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.group.GroupDto;
import ru.tsu.hits.hitsinternship.dto.group.NewGroupDto;
import ru.tsu.hits.hitsinternship.service.GroupService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "Группы")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "Создать группу", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public GroupDto createGroup(@Valid @RequestBody NewGroupDto newGroupDto) {
        return groupService.createGroup(newGroupDto);
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить группу", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить список групп")
    @GetMapping
    public List<GroupDto> getGroups() {
        return groupService.getGroups();
    }

}