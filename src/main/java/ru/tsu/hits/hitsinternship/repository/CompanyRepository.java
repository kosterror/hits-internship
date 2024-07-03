package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.hitsinternship.entity.CompanyEntity;
import ru.tsu.hits.hitsinternship.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {

    List<CompanyEntity> findAllByOrderByNameAsc();

    List<CompanyEntity> findAllByOfficer(UserEntity entity);

}
