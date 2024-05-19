package ru.tsu.hits.hitsinternship.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "change_practice_application")
public class ChangePracticeApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String comment;

    private String notPartner;

    @Enumerated(EnumType.STRING)
    private ChangePracticeApplicationStatus status;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity partner;

    @ManyToOne
    @JoinColumn(name = "checking_employee_id")
    private UserEntity checkingEmployee;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdatedDate;
}
