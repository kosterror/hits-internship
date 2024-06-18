package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.specialties.NewSpecialityDto;
import ru.tsu.hits.hitsinternship.dto.specialties.SpecialityDto;
import ru.tsu.hits.hitsinternship.entity.SpecialityEntity;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.SpecialityMapper;
import ru.tsu.hits.hitsinternship.repository.SpecialityRepository;

import java.util.List;
import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class SpecialityService {

    private final SpecialityMapper specialityMapper;
    private final SpecialityRepository specialityRepository;

    public SpecialityDto createSpeciality(NewSpecialityDto newSpecialityDto) {
        SpecialityEntity speciality = specialityMapper.newDtoToEntity(newSpecialityDto);
        specialityRepository.save(speciality);
        return specialityMapper.entityToDto(speciality);
    }

    public void deleteSpeciality(UUID specialityId) {
        var speciality = findById(specialityId);

        if (!isEmpty(speciality.getPositions()) || !isEmpty(speciality.getCompanyWishes())) {
            throw new ConflictException(
                    "Направление '%s' используется в пожеланиях компаний или позициях студентов"
                            .formatted(speciality.getName())
            );
        }

        specialityRepository.deleteById(specialityId);
    }

    public List<SpecialityDto> getSpecialities() {
        return specialityRepository.findAll().stream()
                .map(specialityMapper::entityToDto)
                .toList();
    }

    private SpecialityEntity findById(UUID id) {
        return specialityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Speciality %s not found".formatted(id)));
    }

}
