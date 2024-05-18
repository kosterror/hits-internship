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
@Table(name = "position")
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PositionStatus positionStatus;

    private Integer priority;

    @ManyToOne
    @JoinColumn(name = "program_language_id")
    private ProgramLanguageEntity programLanguage;

    @ManyToOne
    @JoinColumn(name = "speciality_id")
    private SpecialityEntity speciality;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
