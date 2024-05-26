package ru.tsu.hits.hitsinternship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "meeting")
public class MeetingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PairNumber pairNumber;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalDate date;

    private String audience;

    @ManyToMany
    @JoinTable(
            name = "meeting_group",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<GroupEntity> groups;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

}
