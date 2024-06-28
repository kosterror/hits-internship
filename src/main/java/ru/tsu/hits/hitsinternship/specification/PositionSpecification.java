package ru.tsu.hits.hitsinternship.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.tsu.hits.hitsinternship.entity.*;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class PositionSpecification {

    public static Specification<PositionEntity> hasCompanyIds(List<UUID> companyIds) {
        return (root, query, builder) ->
                root.get(PositionEntity_.COMPANY)
                        .get(CompanyEntity_.ID)
                        .in(companyIds);
    }

    public static Specification<PositionEntity> hasSpecialityIds(List<UUID> specialityIds) {
        return (root, query, builder) ->
                root.get(PositionEntity_.SPECIALITY)
                        .get(SpecialityEntity_.ID)
                        .in(specialityIds);
    }

    public static Specification<PositionEntity> hasProgramLanguageIds(List<UUID> programLanguageIds) {
        return (root, query, builder) ->
                root.get(PositionEntity_.PROGRAM_LANGUAGE)
                        .get(ProgramLanguageEntity_.ID)
                        .in(programLanguageIds);
    }

    public static Specification<PositionEntity> fullNameLike(String fullName) {
        return (root, query, builder) ->
                builder.like(
                        builder.lower(root.get(PositionEntity_.USER).get(UserEntity_.FULL_NAME)),
                        "%" + (fullName == null ? null : fullName.toLowerCase()) + "%"
                );
    }

    public static Specification<PositionEntity> hasGroupIds(List<UUID> groupIds) {
        return (root, query, builder) -> root.get(PositionEntity_.USER)
                .get(UserEntity_.GROUP)
                .get(GroupEntity_.ID)
                .in(groupIds);
    }

    public static Specification<PositionEntity> hasPositionStatus(PositionStatus positionStatus) {
        return (root, query, builder) ->
                builder.equal(root.get(PositionEntity_.POSITION_STATUS), positionStatus);
    }

}
