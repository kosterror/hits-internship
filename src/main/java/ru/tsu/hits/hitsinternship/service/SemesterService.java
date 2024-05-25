package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.semester.NewSemesterDto;
import ru.tsu.hits.hitsinternship.dto.semester.SemesterDto;
import ru.tsu.hits.hitsinternship.entity.SemesterEntity;
import ru.tsu.hits.hitsinternship.entity.SemesterEntity_;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.SemesterMapper;
import ru.tsu.hits.hitsinternship.repository.SemesterRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SemesterService {

    private final SemesterMapper semesterMapper;
    private final SemesterRepository semesterRepository;

    public SemesterDto createSemester(NewSemesterDto newSemesterDto) {
        var entity = semesterMapper.dtoToEntity(newSemesterDto);
        entity = semesterRepository.save(entity);

        return semesterMapper.entityToDto(entity);
    }

    public SemesterDto getSemester(UUID id) {
        return semesterMapper.entityToDto(getSemesterEntity(id));
    }

    public List<SemesterDto> getSemesters() {
        return semesterRepository.findAll(Sort.by(Sort.Direction.ASC,
                        SemesterEntity_.STUDY_YEAR,
                        SemesterEntity_.NUMBER)
                )
                .stream()
                .map(semesterMapper::entityToDto)
                .toList();
    }

    public void deleteSemester(UUID id) {
        semesterRepository.deleteById(id);
    }

    public SemesterEntity getSemesterEntity(UUID id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Semester with id %s not found".formatted(id)));
    }

    public SemesterDto updateSemester(UUID id, NewSemesterDto newSemesterDto) {
        var entity = getSemesterEntity(id);
        entity.setNumber(newSemesterDto.getNumber());
        entity.setStudyYear(newSemesterDto.getStudyYear());
        entity.setStartDate(newSemesterDto.getStartDate());
        entity.setEndDate(newSemesterDto.getEndDate());
        entity.setChangeCompanyApplicationDeadline(newSemesterDto.getChangeCompanyApplicationDeadline());

        entity = semesterRepository.save(entity);
        return semesterMapper.entityToDto(entity);
    }
}
