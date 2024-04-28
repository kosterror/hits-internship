package ru.tsu.hits.hitsinternship.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.hitsinternship.dto.group.GroupDto;
import ru.tsu.hits.hitsinternship.dto.group.NewGroupDto;
import ru.tsu.hits.hitsinternship.service.GroupService;

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

}
