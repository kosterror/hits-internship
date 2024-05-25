package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.hitsinternship.entity.PracticeEntity;

import java.util.List;
import java.util.UUID;

public interface PracticeRepository extends JpaRepository<PracticeEntity, UUID> {

    List<PracticeEntity> findAllByUserId(UUID userId);
}
