package ru.tsu.hits.hitsinternship.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fullName;

    private String email;

    private String password;

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<Role> roles;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @OneToMany(mappedBy = "user")
    private List<PositionEntity> positions;

    @OneToMany(mappedBy = "owner")
    private List<FileMetaInfoEntity> files;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<PracticeEntity> practices;

    @OneToMany(mappedBy = "author")
    @OrderBy("creationDate DESC")
    private List<ChangePracticeApplicationEntity> changePracticeApplicationEntities;

    @OneToMany(mappedBy = "checkingEmployee")
    private List<ChangePracticeApplicationEntity> checkedChangePracticeApplicationEntities;

}