package ru.tsu.hits.hitsinternship.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.practice.BasePracticeDto;
import ru.tsu.hits.hitsinternship.dto.practice.NewPracticeDto;
import ru.tsu.hits.hitsinternship.dto.practice.PracticeDto;
import ru.tsu.hits.hitsinternship.entity.PracticeEntity;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PracticeMapper {

    PracticeEntity newDtoToEntity(NewPracticeDto dto);

    PracticeDto entityToDto(PracticeEntity practice);

    BasePracticeDto entityToBaseDto(PracticeEntity practice);
}
