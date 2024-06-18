package ru.tsu.hits.hitsinternship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "company_wishes")
public class CompanyWishesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String internAmount;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "program_language_id")
    private ProgramLanguageEntity programLanguage;

    @ManyToOne
    @JoinColumn(name = "speciality_id")
    private SpecialityEntity speciality;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
}
