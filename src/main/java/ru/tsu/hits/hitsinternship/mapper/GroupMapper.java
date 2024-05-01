package ru.tsu.hits.hitsinternship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.group.GroupDto;
import ru.tsu.hits.hitsinternship.dto.group.NewGroupDto;
import ru.tsu.hits.hitsinternship.entity.GroupEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GroupMapper {

    GroupEntity newDtoToEntity(NewGroupDto dto);

    GroupDto entityToDto(GroupEntity group);

}
