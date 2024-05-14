package ru.tsu.hits.hitsinternship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.hitsinternship.entity.FileMetaInfoEntity;

import java.util.UUID;

public interface FileMetaInfoRepository extends JpaRepository<FileMetaInfoEntity, UUID> {
}
