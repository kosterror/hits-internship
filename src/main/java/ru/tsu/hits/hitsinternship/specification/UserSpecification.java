package ru.tsu.hits.hitsinternship.specification;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.tsu.hits.hitsinternship.entity.*;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class UserSpecification {

    public static Specification<UserEntity> hasFullName(String fullName) {
        return (root, query, builder) ->
                builder.like(
                        builder.lower(root.get(UserEntity_.FULL_NAME)),
                        "%" + (fullName == null ? null : fullName.toLowerCase()) + "%"
                );
    }

    public static Specification<UserEntity> isActive(Boolean isActive) {
        return (root, query, builder) ->
                builder.equal(
                        root.get(UserEntity_.IS_ACTIVE),
                        isActive
                );
    }

    public static Specification<UserEntity> hasRoles(List<Role> roles) {
        return (root, query, criteriaBuilder) -> root
                .join(UserEntity_.ROLES)
                .in(roles);
    }

    public static Specification<UserEntity> inGroupIds(List<UUID> groupIds) {
        return (root, query, builder) -> root
                .join(UserEntity_.GROUP)
                .get(GroupEntity_.ID)
                .in(groupIds);
    }

    public static Specification<UserEntity> hasNoPracticeInSemesterAndInGroupIds(UUID semesterId,
                                                                                 List<UUID> groupIds
    ) {
        return (root, query, cb) -> {
            Subquery<UUID> practiceSubquery = query.subquery(UUID.class);
            Root<PracticeEntity> practiceRoot = practiceSubquery.from(PracticeEntity.class);
            practiceSubquery.select(practiceRoot.get(PracticeEntity_.USER).get(UserEntity_.ID));
            practiceSubquery.where(
                    cb.equal(practiceRoot.get(PracticeEntity_.SEMESTER).get(SemesterEntity_.ID), semesterId)
            );

            return cb.and(
                    root.get(UserEntity_.GROUP).get(GroupEntity_.ID).in(groupIds),
                    cb.not(root.get(UserEntity_.ID).in(practiceSubquery))
            );
        };
    }

}
