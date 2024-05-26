package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.hitsinternship.entity.ChangePracticeApplicationEntity;
import ru.tsu.hits.hitsinternship.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface ChangePracticeApplicationRepository extends JpaRepository<ChangePracticeApplicationEntity, UUID> {

    List<ChangePracticeApplicationEntity> getAllByAuthor(UserEntity user);
}
