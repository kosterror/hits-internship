package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.tsu.hits.hitsinternship.entity.PracticeEntity;

import java.util.List;
import java.util.UUID;

public interface PracticeRepository
        extends JpaRepository<PracticeEntity, UUID>, JpaSpecificationExecutor<PracticeEntity> {

    List<PracticeEntity> findAllByUserId(UUID userId);

    List<PracticeEntity> findAllBySemesterId(UUID semesterId, Sort sort);

}
