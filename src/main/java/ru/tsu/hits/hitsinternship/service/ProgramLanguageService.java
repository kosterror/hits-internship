package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.programlanguage.NewProgramLanguageDto;
import ru.tsu.hits.hitsinternship.dto.programlanguage.ProgramLanguageDto;
import ru.tsu.hits.hitsinternship.mapper.ProgramLanguageMapper;
import ru.tsu.hits.hitsinternship.repository.ProgramLanguageRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgramLanguageService {

    private final ProgramLanguageRepository programLanguageRepository;

    private final ProgramLanguageMapper programLanguageMapper;

    public ProgramLanguageDto createProgramLanguage(NewProgramLanguageDto newProgramLanguageDto) {
        var programLanguageEntity = programLanguageMapper.newDtoToEntity(newProgramLanguageDto);
        programLanguageRepository.save(programLanguageEntity);
        return programLanguageMapper.entityToDto(programLanguageEntity);
    }

    public void deleteProgramLanguage(UUID programLanguageId) {
        programLanguageRepository.deleteById(programLanguageId);
    }

    public List<ProgramLanguageDto> getProgramLanguages() {
        return programLanguageRepository.findAll().stream()
                .map(programLanguageMapper::entityToDto)
                .toList();
    }


}
