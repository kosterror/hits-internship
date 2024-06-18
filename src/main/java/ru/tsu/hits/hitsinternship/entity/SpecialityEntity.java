package ru.tsu.hits.hitsinternship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "speciality")
public class SpecialityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
}
