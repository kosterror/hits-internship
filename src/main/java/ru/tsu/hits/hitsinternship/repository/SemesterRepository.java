package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.hitsinternship.entity.SemesterEntity;

import java.util.UUID;

public interface SemesterRepository extends JpaRepository<SemesterEntity, UUID> {
}
