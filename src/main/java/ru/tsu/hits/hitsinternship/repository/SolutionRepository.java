package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.hitsinternship.entity.SolutionEntity;

import java.util.UUID;

public interface SolutionRepository extends JpaRepository<SolutionEntity, UUID> {
}
