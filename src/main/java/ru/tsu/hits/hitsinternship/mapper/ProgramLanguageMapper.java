package ru.tsu.hits.hitsinternship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.programlanguage.NewProgramLanguageDto;
import ru.tsu.hits.hitsinternship.dto.programlanguage.ProgramLanguageDto;
import ru.tsu.hits.hitsinternship.entity.ProgramLanguageEntity;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProgramLanguageMapper {

    ProgramLanguageEntity newDtoToEntity(NewProgramLanguageDto dto);

    ProgramLanguageDto entityToDto(ProgramLanguageEntity programLanguage);
}
