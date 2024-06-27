package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.hitsinternship.entity.SolutionEntity;
import ru.tsu.hits.hitsinternship.entity.SolutionState;

import java.util.List;
import java.util.UUID;

public interface SolutionRepository extends JpaRepository<SolutionEntity, UUID> {

    List<SolutionEntity> findAllByTaskIdAndAuthorId(UUID taskId, UUID userId);

    Page<SolutionEntity> findAllByTaskId(UUID taskId, Pageable pageable);

    Page<SolutionEntity> findAllByTaskIdAndStateIn(UUID taskId,
                                                   List<SolutionState> solutionStates,
                                                   Pageable pageable);

    Page<SolutionEntity> findAllByTaskIdAndAuthorFullNameContainsIgnoreCase(UUID taskId,
                                                                            String fullName,
                                                                            Pageable pageable);

    Page<SolutionEntity> findAllByTaskIdAndAuthorFullNameContainsIgnoreCaseAndStateIn(UUID taskId,
                                                                                      String fullName,
                                                                                      List<SolutionState> solutionStates,
                                                                                      Pageable pageable);

}
