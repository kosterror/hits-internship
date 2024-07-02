package ru.tsu.hits.hitsinternship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String websiteLink;

    private Boolean isVisible;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curator_id")
    private UserEntity curator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "officer_id")
    private UserEntity officer;
}
