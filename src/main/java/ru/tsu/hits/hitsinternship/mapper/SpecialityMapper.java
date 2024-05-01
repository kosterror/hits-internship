package ru.tsu.hits.hitsinternship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.specialties.NewSpecialityDto;
import ru.tsu.hits.hitsinternship.dto.specialties.SpecialityDto;
import ru.tsu.hits.hitsinternship.entity.SpecialityEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SpecialityMapper {

    SpecialityEntity newDtoToEntity(NewSpecialityDto dto);

    SpecialityDto entityToDto(SpecialityEntity group);
}
