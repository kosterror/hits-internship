package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.specialties.NewSpecialityDto;
import ru.tsu.hits.hitsinternship.dto.specialties.SpecialityDto;
import ru.tsu.hits.hitsinternship.entity.SpecialityEntity;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.SpecialityMapper;
import ru.tsu.hits.hitsinternship.repository.SpecialityRepository;

import java.util.List;
import java.util.UUID;

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

        if (!specialityRepository.existsById(specialityId)) {
            throw new NotFoundException("Специальности с таким id не существует");
        }
        specialityRepository.deleteById(specialityId);

    }

    public List<SpecialityDto> getSpecialities() {

        return specialityRepository.findAll().stream()
                .map(specialityMapper::entityToDto)
                .toList();
    }

}
