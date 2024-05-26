package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.tsu.hits.hitsinternship.entity.MeetingEntity;

import java.util.UUID;

public interface MeetingRepository extends JpaRepository<MeetingEntity, UUID>, JpaSpecificationExecutor<MeetingEntity> {
}
