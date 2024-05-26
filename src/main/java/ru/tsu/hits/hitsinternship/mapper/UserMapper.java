package ru.tsu.hits.hitsinternship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;
import ru.tsu.hits.hitsinternship.entity.UserEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "isActive", source = "active")
    @Mapping(target = "groupId", source = "group.id")
    UserDto entityToDto(UserEntity entity);

}
