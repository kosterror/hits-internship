package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.hitsinternship.entity.ProgramLanguageEntity;

import java.util.UUID;

@Repository
public interface ProgramLanguageRepository extends JpaRepository<ProgramLanguageEntity, UUID> {
}
