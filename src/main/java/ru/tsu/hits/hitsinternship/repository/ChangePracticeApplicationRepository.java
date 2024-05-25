package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.hitsinternship.entity.ChangePracticeApplicationEntity;

import java.util.UUID;

public interface ChangePracticeApplicationRepository extends JpaRepository<ChangePracticeApplicationEntity, UUID> {

}
