package ru.tsu.hits.hitsinternship.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "group_")
public class groupEntity {


    @Id
    private UUID id;

    private String name;
}
