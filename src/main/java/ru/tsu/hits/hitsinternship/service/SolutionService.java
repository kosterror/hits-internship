package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.NewSolutionDto;
import ru.tsu.hits.hitsinternship.dto.PaginationResponse;
import ru.tsu.hits.hitsinternship.dto.SolutionDto;
import ru.tsu.hits.hitsinternship.entity.SolutionEntity;
import ru.tsu.hits.hitsinternship.entity.SolutionEntity_;
import ru.tsu.hits.hitsinternship.entity.SolutionState;
import ru.tsu.hits.hitsinternship.entity.TaskEntity;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.SolutionMapper;
import ru.tsu.hits.hitsinternship.repository.SolutionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionMapper solutionMapper;
    private final UserService userService;
    private final TaskService taskService;
    private final FileStorageService fileStorageService;
    private final SolutionRepository solutionRepository;

    public SolutionDto createSolution(UUID taskId,
                                      NewSolutionDto newSolutionDto,
                                      UUID userId) {
        var task = taskService.getTaskEntity(taskId);
        checkTaskDeadline(task);
        checkExistingSolutions(taskId, userId);

        var user = userService.getUserEntityById(userId);

        var solutionBuilder = SolutionEntity.builder()
                .task(task)
                .author(user)
                .dateTime(LocalDateTime.now())
                .state(SolutionState.NEW);

        if (newSolutionDto.getFileIds() != null) {
            var files = fileStorageService.getFileMetaInfoEntities(newSolutionDto.getFileIds());

            solutionBuilder.files(files);
        }

        if (newSolutionDto.getComment() != null) {
            solutionBuilder.comment(newSolutionDto.getComment());
        }

        var solution = solutionRepository.save(solutionBuilder.build());

        return solutionMapper.toDto(solution);
    }

    private void checkExistingSolutions(UUID taskId, UUID userId) {
        var existedSolutions = solutionRepository.findAllByTaskIdAndAuthorId(taskId, userId);
        if (!existedSolutions.isEmpty()) {
            throw new ConflictException("У вас уже есть решение на это задание");
        }
    }

    private void checkTaskDeadline(TaskEntity task) {
        if (task.getDeadline().isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дедлайн прошел");
        }
    }

    public PaginationResponse<SolutionDto> getTaskSolutions(UUID taskId,
                                                            String fullName,
                                                            List<SolutionState> solutionStates,
                                                            int page,
                                                            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, SolutionEntity_.DATE_TIME);
        Page<SolutionEntity> solutions;


        if (fullName == null && solutionStates == null) {
            solutions = solutionRepository.findAllByTaskId(taskId, pageable);
        } else if (fullName == null) {
            solutions = solutionRepository.findAllByTaskIdAndStateIn(taskId, solutionStates, pageable);
        } else if (solutionStates == null) {
            solutions = solutionRepository.findAllByTaskIdAndAuthorFullNameContainsIgnoreCase(taskId,
                    fullName,
                    pageable
            );
        } else {
            solutions = solutionRepository.findAllByTaskIdAndAuthorFullNameContainsIgnoreCaseAndStateIn(taskId,
                    fullName,
                    solutionStates,
                    pageable
            );
        }

        List<SolutionDto> solutionDtos = solutions.stream()
                .map(solutionMapper::toDto)
                .toList();

        return PaginationResponse.<SolutionDto>builder()
                .pageNumber(page)
                .pageSize(size)
                .elements(solutionDtos)
                .totalSize(solutions.getTotalElements())
                .build();
    }

    public List<SolutionDto> getMySolutions(UUID taskId,
                                            UUID userId) {
        return solutionRepository.findAllByTaskIdAndAuthorId(taskId, userId)
                .stream()
                .map(solutionMapper::toDto)
                .toList();
    }

    public SolutionDto updateSolution(UUID taskId,
                                      UUID solutionId,
                                      NewSolutionDto newSolutionDto,
                                      UUID userId) {
        var solution = getSolutionEntityByTaskIdAndSolutionIdAndUserId(taskId, solutionId, userId);
        checkTaskDeadline(solution.getTask());

        if (solution.getState() == SolutionState.ACCEPTED) {
            throw new ConflictException("Нельзя изменить решение после его принятия");
        }

        if (newSolutionDto.getFileIds() != null) {
            var files = fileStorageService.getFileMetaInfoEntities(newSolutionDto.getFileIds());
            solution.setFiles(files);
        } else {
            solution.setFiles(null);
        }

        if (newSolutionDto.getComment() != null) {
            solution.setComment(newSolutionDto.getComment());
        }

        solution.setDateTime(LocalDateTime.now());
        solution = solutionRepository.save(solution);

        return solutionMapper.toDto(solution);
    }

    public SolutionDto acceptSolution(UUID taskId,
                                      UUID solutionId,
                                      int mark) {
        var solution = getSolutionEntityByTaskIdAndSolutionId(taskId, solutionId);

        solution.setMark(mark);
        solution.setState(SolutionState.ACCEPTED);

        solution = solutionRepository.save(solution);

        return solutionMapper.toDto(solution);
    }

    public SolutionDto rejectSolution(UUID taskId,
                                      UUID solutionId) {
        var solution = getSolutionEntityByTaskIdAndSolutionId(taskId, solutionId);

        solution.setMark(0);
        solution.setState(SolutionState.REJECTED);

        solution = solutionRepository.save(solution);

        return solutionMapper.toDto(solution);
    }

    private SolutionEntity getSolutionEntityByTaskIdAndSolutionId(UUID taskId,
                                                                  UUID solutionId) {
        var solution = solutionRepository
                .findById(solutionId)
                .orElseThrow(() -> throwSolutionNotFoundException(solutionId));

        if (!taskId.equals(solution.getTask().getId())) {
            throw throwSolutionNotFoundException(solutionId);
        }
        return solution;
    }

    private SolutionEntity getSolutionEntityByTaskIdAndSolutionIdAndUserId(UUID taskId,
                                                                           UUID solutionId,
                                                                           UUID userId) {
        var solution = getSolutionEntity(solutionId);
        if (!taskId.equals(solution.getTask().getId()) || !userId.equals(solution.getAuthor().getId())) {
            throw throwSolutionNotFoundException(solutionId);
        }

        return solution;
    }

    private SolutionEntity getSolutionEntity(UUID id) {
        return solutionRepository.findById(id)
                .orElseThrow(() -> throwSolutionNotFoundException(id));
    }

    private NotFoundException throwSolutionNotFoundException(UUID solutionId) {
        return new NotFoundException("Solution with id %s not found".formatted(solutionId));
    }
}
