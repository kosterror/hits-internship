package ru.tsu.hits.hitsinternship.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.tsu.hits.hitsinternship.entity.GroupEntity_;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.entity.UserEntity_;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class UserSpecification {

    public static Specification<UserEntity> hasFullName(String fullName) {
        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
                -> criteriaBuilder.like(root.get(UserEntity_.FULL_NAME), "%" + fullName + "%");
    }

    public static Specification<UserEntity> isActive(Boolean isActive) {
        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
                -> criteriaBuilder.equal(root.get(UserEntity_.IS_ACTIVE), isActive);
    }

    public static Specification<UserEntity> hasRoles(List<Role> roles) {
        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
                -> root.join(UserEntity_.ROLES).in(roles);
    }

    public static Specification<UserEntity> inGroupIds(List<UUID> groupIds) {
        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
                -> root.join(UserEntity_.GROUP).get(GroupEntity_.ID).in(groupIds);
    }

}
