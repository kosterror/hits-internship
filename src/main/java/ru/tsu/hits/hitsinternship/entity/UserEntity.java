package ru.tsu.hits.hitsinternship.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
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

    @ElementCollection
    @CollectionTable(name = "user_refresh_token", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "refresh_token")
    private List<String> refreshTokens;

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

    @OneToMany(mappedBy = "user")
    private List<PracticeEntity> practices;

    @OneToMany(mappedBy = "author")
    private List<ChangePracticeApplication> changePracticeApplications;

    @OneToMany(mappedBy = "checkingEmployee")
    private List<ChangePracticeApplication> checkedChangePracticeApplications;

}
