package ru.tsu.hits.hitsinternship.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "program_language")
public class ProgramLanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "programLanguage")
    private List<CompanyWishesEntity> companyWishes;

    @OneToMany(mappedBy = "programLanguage")
    private List<PositionEntity> positions;

}
