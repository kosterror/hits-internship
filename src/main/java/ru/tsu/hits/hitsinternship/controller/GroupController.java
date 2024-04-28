package ru.tsu.hits.hitsinternship.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "Создать группу")
    @PostMapping("")
    GroupDto createGroup(@Valid @RequestBody NewGroupDto newGroupDto) {

        return groupService.createGroup(newGroupDto);
    }

    @Operation(summary = "Удалить группу")
    @DeleteMapping("/{groupId}")
    void deleteGroup(@Valid @PathVariable UUID groupId) {

        groupService.deleteGroup(groupId);
    }

    @Operation(summary = "Получить список групп")
    @GetMapping("")
    List<GroupDto> getGroups() {
        return groupService.getGroups();
    }

}