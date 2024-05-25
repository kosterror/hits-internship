package ru.tsu.hits.hitsinternship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.changepracticeapplication.ChangePracticeApplicationDto;
import ru.tsu.hits.hitsinternship.dto.changepracticeapplication.NewApplicationDto;
import ru.tsu.hits.hitsinternship.entity.ChangePracticeApplicationEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChangePracticeApplicationMapper {

    ChangePracticeApplicationEntity newDtoToEntity(NewApplicationDto dto);

    ChangePracticeApplicationDto entityToDto(ChangePracticeApplicationEntity companyWishes);
}