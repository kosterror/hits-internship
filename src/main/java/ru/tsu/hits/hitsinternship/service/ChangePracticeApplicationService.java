package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.changepracticeapplication.ChangePracticeApplicationDto;
import ru.tsu.hits.hitsinternship.dto.changepracticeapplication.NewApplicationDto;
import ru.tsu.hits.hitsinternship.dto.company.NewCompanyDto;
import ru.tsu.hits.hitsinternship.dto.practice.NewPracticeDto;
import ru.tsu.hits.hitsinternship.entity.ChangePracticeApplicationEntity;
import ru.tsu.hits.hitsinternship.entity.ChangePracticeApplicationStatus;
import ru.tsu.hits.hitsinternship.entity.SemesterEntity;
import ru.tsu.hits.hitsinternship.exception.BadRequestException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.ChangePracticeApplicationMapper;
import ru.tsu.hits.hitsinternship.repository.ChangePracticeApplicationRepository;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChangePracticeApplicationService {

    private final ChangePracticeApplicationRepository changePracticeApplicationRepository;
    private final ChangePracticeApplicationMapper changePracticeApplicationMapper;
    private final UserService userService;
    private final CompanyService companyService;
    private final SemesterService semesterService;
    private final PositionService positionService;
    private final PracticeService practiceService;

    public void deleteChangePracticeApplication(UUID applicationId, UUID userId) {

        ChangePracticeApplicationEntity applicationEntity = getChangePracticeApplicationEntity(applicationId);
        checkStatus(applicationEntity);
        positionService.checkPermissionWithRole(userId, applicationEntity.getAuthor().getId());
        changePracticeApplicationRepository.deleteById(applicationId);
    }

    public ChangePracticeApplicationDto createChangePracticeApplication(NewApplicationDto newApplicationDto, UUID authorId) {

        checkChangePracticeApplication(newApplicationDto);
        ChangePracticeApplicationEntity application = changePracticeApplicationMapper
                .newDtoToEntity(newApplicationDto);

        application.setAuthor(userService.getUserEntityById(authorId));
        application.setPartner(companyService.findCompanyById(newApplicationDto.getCompanyId()));
        application.setSemester(semesterService.getSemesterEntity(newApplicationDto.getSemesterId()));
        checkDeadline(application.getSemester());
        application.setStatus(ChangePracticeApplicationStatus.QUEUE);
        application.setCreationDate(LocalDateTime.now());
        application.setLastUpdatedDate(LocalDateTime.now());
        changePracticeApplicationRepository.save(application);
        return changePracticeApplicationMapper.entityToDto(application);
    }

    public ChangePracticeApplicationDto makeDecisionOnApplication(
            UUID applicationId,
            UUID checkingEmployeeId,
            ChangePracticeApplicationStatus changePracticeApplicationStatus) {

        ChangePracticeApplicationEntity application = getChangePracticeApplicationEntity(applicationId);
        application.setStatus(changePracticeApplicationStatus);
        application.setLastUpdatedDate(LocalDateTime.now());
        application.setCheckingEmployee(userService.getUserEntityById(checkingEmployeeId));
        if (changePracticeApplicationStatus.equals(ChangePracticeApplicationStatus.APPROVED)) {
            createPractice(application);
        }
        changePracticeApplicationRepository.save(application);
        return changePracticeApplicationMapper.entityToDto(application);
    }

    public List<ChangePracticeApplicationDto> getChangePracticeApplications() {

        return changePracticeApplicationRepository.findAll().stream()
                .map(changePracticeApplicationMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public List<ChangePracticeApplicationDto> getStudentChangePracticeApplications(UUID userId) {

        return changePracticeApplicationRepository.getAllByAuthor(userService.getUserEntityById(userId)).stream()
                .map(changePracticeApplicationMapper::entityToDto)
                .collect(Collectors.toList());
    }

    private ChangePracticeApplicationEntity getChangePracticeApplicationEntity(UUID applicationId) {
        return changePracticeApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Заявка на изменение места практики не найдена"));
    }

    private void createPractice(ChangePracticeApplicationEntity changePracticeApplicationEntity) {

        NewPracticeDto newPracticeDto = new NewPracticeDto(
                null,
                changePracticeApplicationEntity.getSemester().getId(),
                changePracticeApplicationEntity.getAuthor().getId(),
                getCompanyId(changePracticeApplicationEntity));
        practiceService.createPractice(newPracticeDto);
    }

    private void checkChangePracticeApplication(NewApplicationDto newApplicationDto) {

        if (newApplicationDto.getCompanyId() != null && newApplicationDto.getNotPartner() != null) {
            throw new BadRequestException("Можно задать только одно из полей companyId или notPartner");
        }

        if (newApplicationDto.getCompanyId() == null && newApplicationDto.getNotPartner() == null) {
            throw new BadRequestException("Нужно задать хотя бы одно из полей companyId или notPartner");
        }
    }

    private void checkDeadline(SemesterEntity semester) {

        if (semester.getChangeCompanyApplicationDeadline().isAfter(ChronoLocalDateTime.from(LocalDateTime.now()))) {
            throw new BadRequestException("Срок подачи заявок на изменение места практики истек");
        }
    }

    private UUID getCompanyId(ChangePracticeApplicationEntity changePracticeApplicationEntity) {
        if (changePracticeApplicationEntity.getNotPartner() != null) {
            NewCompanyDto newCompanyDto = new NewCompanyDto(changePracticeApplicationEntity.getNotPartner(), null, false);
            return companyService.createCompany(newCompanyDto).getId();
        } else {
            return changePracticeApplicationEntity.getPartner().getId();
        }
    }

    private void checkStatus(ChangePracticeApplicationEntity changePracticeApplicationEntity) {

        if (!changePracticeApplicationEntity.getStatus().equals(ChangePracticeApplicationStatus.QUEUE)) {
            throw new BadRequestException("Нельзя изменить заявку с данным статусом");
        }
    }

}