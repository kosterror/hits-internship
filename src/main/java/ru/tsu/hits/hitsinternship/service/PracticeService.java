package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.practice.EditPracticeDto;
import ru.tsu.hits.hitsinternship.dto.practice.NewPracticeDto;
import ru.tsu.hits.hitsinternship.dto.practice.PracticeDto;
import ru.tsu.hits.hitsinternship.dto.practice.PracticeReportDto;
import ru.tsu.hits.hitsinternship.entity.PracticeEntity;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.exception.BadRequestException;
import ru.tsu.hits.hitsinternship.exception.InternalException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.PracticeMapper;
import ru.tsu.hits.hitsinternship.mapper.UserMapper;
import ru.tsu.hits.hitsinternship.repository.PracticeRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PracticeService {

    private final PracticeRepository practiceRepository;

    private final PracticeMapper practiceMapper;

    private final UserMapper userMapper;

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
        checkPractice(newPracticeDto);
        PracticeEntity practiceEntity = practiceMapper.newDtoToEntity(newPracticeDto);
        practiceEntity.setSemester(semesterService.getSemesterEntity(newPracticeDto.getSemesterId()));
        practiceEntity.setUser(userService.getUserEntityById(newPracticeDto.getUserId()));
        practiceEntity.setCompany(companyService.findCompanyById(newPracticeDto.getCompanyId()));
        practiceRepository.save(practiceEntity);
        return practiceMapper.entityToDto(practiceEntity);
    }

    private void checkPractice(NewPracticeDto newPracticeDto) {
        practiceRepository.findAllByUserId(userService.getUserEntityById(newPracticeDto.getUserId()).getId())
                .forEach(practice -> {
                    if (practice.getSemester().getId().equals(newPracticeDto.getSemesterId())) {
                        throw new BadRequestException("User already has practice in this semester");
                    }
                });
    }

    public List<PracticeDto> getSemesterPractices(UUID semesterId) {
        return practiceRepository.findAllBySemesterId(
                        semesterId,
                        Sort.by(Sort.Order.asc("user.fullName"))
                ).stream()
                .map(practiceMapper::entityToDto)
                .toList();
    }

    public PracticeReportDto getPractices(UUID semesterId, List<UUID> groupIds) {
        List<UserEntity> studentsFromGroups = userService.getUsersByGroupIds(groupIds);

        var studentsWithPractice = studentsFromGroups.stream()
                .filter(student ->
                        student.getPractices()
                                .stream()
                                .anyMatch(practice -> semesterId.equals(practice.getSemester().getId()))
                ).toList();

        studentsFromGroups.removeAll(studentsWithPractice);
        var studentsWithoutPractice = studentsFromGroups.stream()
                .map(userMapper::entityToDto)
                .toList();

        var practices = studentsWithPractice.stream()
                .map(student -> {
                            var practice =
                                    student.getPractices()
                                            .stream()
                                            .filter(p -> semesterId.equals(p.getSemester().getId()))
                                            .findAny()
                                            .orElseThrow(() -> new InternalException("Failed to find needed practice in already filtered practices"));

                            return practiceMapper.entityToDto(practice);
                        }
                ).toList();

        return new PracticeReportDto(practices, studentsWithoutPractice);
    }
}
