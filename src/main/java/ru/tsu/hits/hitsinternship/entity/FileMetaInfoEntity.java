package ru.tsu.hits.hitsinternship.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "file_meta_info")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaInfoEntity {

    @Id
    private UUID id;

    private String bucket;

    private String name;

    private double size;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    private LocalDateTime uploadDateTime;

}
