package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.hitsinternship.entity.PositionEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, UUID>,
        JpaSpecificationExecutor<PositionEntity> {

    List<PositionEntity> findAllByUserId(UUID userId);
}
