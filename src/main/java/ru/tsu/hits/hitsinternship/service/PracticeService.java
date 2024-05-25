package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.practice.EditPracticeDto;
import ru.tsu.hits.hitsinternship.dto.practice.NewPracticeDto;
import ru.tsu.hits.hitsinternship.dto.practice.PracticeDto;
import ru.tsu.hits.hitsinternship.entity.PracticeEntity;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.PracticeMapper;
import ru.tsu.hits.hitsinternship.repository.PracticeRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PracticeService {

    private final PracticeRepository practiceRepository;

    private final PracticeMapper practiceMapper;

    private final PositionService positionService;

    private final CompanyService companyService;

    private final SemesterService semesterService;

    private final UserService userService;


    public List<PracticeDto> getStudentPractices(UUID userId, UUID targetUserId) {
        positionService.checkPermissionWithRole(userId, targetUserId);

        List<PracticeEntity> practiceEntities = practiceRepository.findAllByUserId(targetUserId);
        return practiceEntities.stream()
                .map(practiceMapper::entityToDto)
                .toList();
    }

    public PracticeDto updatePractice(UUID practiceId, EditPracticeDto newPracticeDto) {
        PracticeEntity practiceEntity = practiceRepository.findById(practiceId)
                .orElseThrow(() -> new NotFoundException("Practice with id " + practiceId + " not found"));
        practiceEntity.setComment(newPracticeDto.getComment());
        practiceEntity.setCompany(companyService.findCompanyById(newPracticeDto.getCompanyId()));
        practiceRepository.save(practiceEntity);
        return practiceMapper.entityToDto(practiceEntity);
    }

    public PracticeDto createPractice(NewPracticeDto newPracticeDto) {

        PracticeEntity practiceEntity = practiceMapper.newDtoToEntity(newPracticeDto);
        practiceEntity.setSemester(semesterService.getSemesterEntity(newPracticeDto.getSemesterId()));
        practiceEntity.setUser(userService.getUserEntityById(newPracticeDto.getUserId()));
        practiceEntity.setCompany(companyService.findCompanyById(newPracticeDto.getCompanyId()));
        practiceRepository.save(practiceEntity);
        return practiceMapper.entityToDto(practiceEntity);
    }


}
