package ru.tsu.hits.hitsinternship.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "semester")
public class SemesterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer number;

    private String studyYear;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime changeCompanyApplicationDeadline;
}
