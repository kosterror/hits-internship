package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.hitsinternship.entity.TaskEntity;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findAllBySemesterId(UUID semesterId, Sort sort);

}
