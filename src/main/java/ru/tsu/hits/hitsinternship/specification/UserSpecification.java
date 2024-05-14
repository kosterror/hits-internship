package ru.tsu.hits.hitsinternship.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class UserSpecification {

    public static Specification<UserEntity> hasFullName(String fullName) {
        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
                -> criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%");
    }

    public static Specification<UserEntity> isActive(Boolean isActive) {
        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
                -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<UserEntity> hasRoles(List<Role> roles) {
        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
                -> root.join("roles").in(roles);
    }

    public static Specification<UserEntity> inGroupIds(List<UUID> groupIds) {
        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
                -> root.join("group").get("id").in(groupIds);
    }

}
