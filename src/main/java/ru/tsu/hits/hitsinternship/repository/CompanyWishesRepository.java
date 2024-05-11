package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.hitsinternship.entity.CompanyWishesEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyWishesRepository extends JpaRepository<CompanyWishesEntity, UUID> {

    List<CompanyWishesEntity> findCompanyWishesEntitiesByCompanyId(UUID companyId);

}