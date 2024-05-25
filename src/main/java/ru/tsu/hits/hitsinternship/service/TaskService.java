package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.task.NewTaskDto;
import ru.tsu.hits.hitsinternship.dto.task.TaskDto;
import ru.tsu.hits.hitsinternship.entity.FileMetaInfoEntity;
import ru.tsu.hits.hitsinternship.entity.TaskEntity;
import ru.tsu.hits.hitsinternship.entity.TaskEntity_;
import ru.tsu.hits.hitsinternship.exception.BadRequestException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.TaskMapper;
import ru.tsu.hits.hitsinternship.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final SemesterService semesterService;

    public TaskDto createTask(UUID userId, NewTaskDto newTaskDto) {
        var entity = taskMapper.toEntity(newTaskDto);
        var user = userService.getUserEntityById(userId);

        if (newTaskDto.getFiles() != null && !newTaskDto.getFiles().isEmpty()) {
            var files = fileStorageService.getFileMetaInfoEntities(newTaskDto.getFiles());
            checkFiles(newTaskDto.getFiles(), files);

            entity.setFiles(files);
        }

        var semester = semesterService.getSemesterEntity(newTaskDto.getSemesterId());

        entity.setCreatedAt(LocalDateTime.now());
        entity.setAuthor(user);
        entity.setSemester(semester);

        taskRepository.save(entity);

        return taskMapper.toDto(entity);
    }


    private void checkFiles(List<UUID> fileIds, List<FileMetaInfoEntity> files) {
        if (files.size() != fileIds.size()) {
            var foundFileIds = files.stream()
                    .map(FileMetaInfoEntity::getId)
                    .toList();

            var notFoundFilesUds = new ArrayList<>(fileIds);
            notFoundFilesUds.removeAll(foundFileIds);

            throw new BadRequestException("Not found files with ids: %s".formatted(notFoundFilesUds));
        }
    }

    public TaskDto getTask(UUID id) {
        TaskEntity taskEntity = getTaskEntity(id);
        return taskMapper.toDto(taskEntity);
    }

    private TaskEntity getTaskEntity(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id %s not found".formatted(id)));
    }

    public List<TaskDto> getTasks(UUID semesterId) {
        return taskRepository.findAllBySemesterId(
                        semesterId,
                        Sort.by(Sort.Direction.DESC, TaskEntity_.CREATED_AT)
                ).stream()
                .map(taskMapper::toDto)
                .toList();
    }
}