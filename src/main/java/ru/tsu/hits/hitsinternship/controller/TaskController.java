package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.hitsinternship.dto.task.MyTaskDto;
import ru.tsu.hits.hitsinternship.dto.task.NewTaskDto;
import ru.tsu.hits.hitsinternship.dto.task.TaskDto;
import ru.tsu.hits.hitsinternship.service.TaskService;

import java.util.List;
import java.util.UUID;

import static ru.tsu.hits.hitsinternship.util.SecurityUtil.extractId;

@Tag(name = "Задачи")
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('DEAN_OFFICER')")
    @Operation(summary = "Создать задачу", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public TaskDto createTask(@Valid @RequestBody NewTaskDto newTaskDto) {
        return taskService.createTask(extractId(), newTaskDto);
    }

    @Operation(summary = "Получить задачу по id", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable UUID id) {
        return taskService.getTask(id);
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'CURATOR', 'DEAN_OFFICER', 'COMPANY_OFFICER')")
    @Operation(summary = "Получить задания, доступные мне", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/my")
    public List<MyTaskDto> getMyTasks() {
        return taskService.getMyTasks(extractId());
    }

    @PreAuthorize("hasRole('DEAN_OFFICER')")
    @Operation(summary = "Удалить задачу", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить все задачи для семестра", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public List<TaskDto> getTasks(@RequestParam UUID semesterId) {
        return taskService.getTasks(semesterId);
    }

}
