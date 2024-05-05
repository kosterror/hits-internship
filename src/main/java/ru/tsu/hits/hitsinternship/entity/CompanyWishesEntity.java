package ru.tsu.hits.hitsinternship.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
