package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.programlanguage.NewProgramLanguageDto;
import ru.tsu.hits.hitsinternship.dto.programlanguage.ProgramLanguageDto;
import ru.tsu.hits.hitsinternship.entity.ProgramLanguageEntity;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.ProgramLanguageMapper;
import ru.tsu.hits.hitsinternship.repository.ProgramLanguageRepository;

import java.util.List;
import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;

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
        var programLanguage = findById(programLanguageId);
        if (!isEmpty(programLanguage.getCompanyWishes()) || !isEmpty(programLanguage.getPositions())) {
            throw new ConflictException(
                    "Язык программирования '%s' используется в пожеланиях компании или позициях"
                            .formatted(programLanguageId)
            );
        }

        programLanguageRepository.delete(programLanguage);
    }

    public List<ProgramLanguageDto> getProgramLanguages() {
        return programLanguageRepository.findAll().stream()
                .map(programLanguageMapper::entityToDto)
                .toList();
    }

    private ProgramLanguageEntity findById(UUID id) {
        return programLanguageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Program language %s not found".formatted(id)));
    }


}
