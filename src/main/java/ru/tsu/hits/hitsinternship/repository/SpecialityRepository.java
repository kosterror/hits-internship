package ru.tsu.hits.hitsinternship.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.hitsinternship.entity.SpecialityEntity;

import java.util.UUID;

@Repository
public interface SpecialityRepository extends JpaRepository<SpecialityEntity, UUID> {
}
