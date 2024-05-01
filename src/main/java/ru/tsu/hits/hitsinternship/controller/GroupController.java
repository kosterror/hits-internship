package ru.tsu.hits.hitsinternship.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.group.GroupDto;
import ru.tsu.hits.hitsinternship.dto.group.NewGroupDto;
import ru.tsu.hits.hitsinternship.service.GroupService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "Группы")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "Создать группу", security = @SecurityRequirement(name = "Bearer Authentication"))
    @PostMapping
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public GroupDto createGroup(@Valid @RequestBody NewGroupDto newGroupDto) {
        return groupService.createGroup(newGroupDto);
    }

    @Operation(summary = "Удалить группу", security = @SecurityRequirement(name = "Bearer Authentication"))
    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasRole('DEAN_OFFICER')")
    public void deleteGroup(@PathVariable UUID groupId) {
        groupService.deleteGroup(groupId);
    }

    @Operation(summary = "Получить список групп")
    @GetMapping
    public List<GroupDto> getGroups() {
        return groupService.getGroups();
    }

}