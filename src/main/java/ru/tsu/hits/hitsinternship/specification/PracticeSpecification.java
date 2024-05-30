package ru.tsu.hits.hitsinternship.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.tsu.hits.hitsinternship.entity.*;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class PracticeSpecification {

    public static Specification<PracticeEntity> hasSemester(UUID semesterId) {
        return (root, query, builder) ->
                builder.equal(
                        root.get(PracticeEntity_.SEMESTER)
                                .get(SemesterEntity_.ID),
                        semesterId
                );
    }

    public static Specification<PracticeEntity> userInGroupIds(List<UUID> groupIds) {
        return (root, query, builder) ->
                root.get(PracticeEntity_.USER)
                        .get(UserEntity_.GROUP)
                        .get(GroupEntity_.ID)
                        .in(groupIds);
    }

    public static Specification<PracticeEntity> userNotIn(List<UUID> userIds) {
        return (root, query, builder) ->
                builder.not(
                        root.get(PositionEntity_.USER)
                                .get(UserEntity_.ID).in(userIds)
                );
    }
}