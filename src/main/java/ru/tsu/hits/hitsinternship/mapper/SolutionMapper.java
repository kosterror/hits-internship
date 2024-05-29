package ru.tsu.hits.hitsinternship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.SolutionDto;
import ru.tsu.hits.hitsinternship.entity.SolutionEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SolutionMapper {

    @Mapping(target = "lastUpdateDateTime", source = "dateTime")
    SolutionDto toDto(SolutionEntity entity);

}
