package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.BorderStyle;
import org.dhatim.fastexcel.Color;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.hitsinternship.dto.practice.*;
import ru.tsu.hits.hitsinternship.dto.user.SemesterReport;
import ru.tsu.hits.hitsinternship.entity.Mark;
import ru.tsu.hits.hitsinternship.entity.PracticeEntity;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.exception.BadRequestException;
import ru.tsu.hits.hitsinternship.exception.InternalException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.PracticeMapper;
import ru.tsu.hits.hitsinternship.mapper.UserMapper;
import ru.tsu.hits.hitsinternship.repository.PracticeRepository;
import ru.tsu.hits.hitsinternship.repository.UserRepository;
import ru.tsu.hits.hitsinternship.specification.PracticeSpecification;
import ru.tsu.hits.hitsinternship.specification.UserSpecification;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PracticeService {

    public static final String GROUP_LABEL = "Группа";
    public static final String FULL_NAME_LABEL = "ФИО";
    public static final String COMPANY_LABEL = "Компания";
    public static final String WORKSHEET_NAME = "Лист 1";
    public static final String APP_VERSION = "1.0";
    private final TaskService taskService;

    private final PracticeRepository practiceRepository;
    private final PracticeMapper practiceMapper;
    private final UserMapper userMapper;
    private final PositionService positionService;
    private final CompanyService companyService;
    private final SemesterService semesterService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final SolutionService solutionService;
    @Value("${spring.application.name}")
    private String appName;


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
                .map(student -> userMapper.entityToDto(
                                student,
                                userService.getCurrentPractice(student)
                        )
                ).toList();

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

    public byte[] downloadPracticesAndMarks(UUID semesterId, List<UUID> groupIds) throws IOException {
        var report = getPractices(semesterId, groupIds);
        var users = new ArrayList<SemesterReport>(
                report.getPractices().size() + report.getStudentsWithoutPractice().size()
        );

        report.getPractices()
                .forEach(practice -> users.add(
                                new SemesterReport(practice.getUser(), practice, new ArrayList<>())
                        )
                );

        report.getStudentsWithoutPractice()
                .forEach(user -> users.add(
                                new SemesterReport(user, null, new ArrayList<>())
                        )
                );

        var tasks = taskService.getTasks(semesterId);

        for (var task : tasks) {
            var taskSolutions = solutionService.getTaskSolutions(
                    task.getId(),
                    null,
                    null,
                    0,
                    Integer.MAX_VALUE
            ).getElements();

            for (var user : users) {
                var solution = taskSolutions.stream()
                        .filter(s -> s.getAuthor().getId().equals(user.getUser().getId()))
                        .findAny()
                        .orElse(null);

                user.getMarks().add(solution == null ? null : solution.getMark());
            }
        }

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             Workbook book = new Workbook(os, appName, APP_VERSION);
             Worksheet worksheet = book.newWorksheet(WORKSHEET_NAME)) {
            worksheet.value(0, 0, GROUP_LABEL);
            worksheet.value(0, 1, FULL_NAME_LABEL);
            worksheet.value(0, 2, COMPANY_LABEL);

            worksheet.style(0, 0).borderStyle(BorderStyle.THIN).borderColor(Color.SMOKY_BLACK).set();
            worksheet.style(0, 1).borderStyle(BorderStyle.THIN).borderColor(Color.SMOKY_BLACK).set();
            worksheet.style(0, 2).borderStyle(BorderStyle.THIN).borderColor(Color.SMOKY_BLACK).set();

            for (int i = 0; i < tasks.size(); i++) {
                worksheet.value(0, i + 3, tasks.get(i).getName());
                worksheet.style(0, i + 3).borderStyle(BorderStyle.THIN).borderColor(Color.SMOKY_BLACK).set();
            }

            for (int i = 0; i < users.size(); i++) {
                var user = users.get(i);
                worksheet.value(i + 1, 0, user.getUser().getGroup().getName());
                worksheet.value(i + 1, 1, user.getUser().getFullName());
                worksheet.value(i + 1, 2, user.getPractice() == null ? "нет компании" : user.getPractice().getCompany().getName());

                worksheet.style(i + 1, 0).borderStyle(BorderStyle.THIN).borderColor(Color.SMOKY_BLACK).set();
                worksheet.style(i + 1, 1).borderStyle(BorderStyle.THIN).borderColor(Color.SMOKY_BLACK).set();
                worksheet.style(i + 1, 2).borderStyle(BorderStyle.THIN).borderColor(Color.SMOKY_BLACK).set();

                if (user.getPractice() == null) {
                    worksheet.style(i + 1, 1)
                            .fillColor(Color.YELLOW)
                            .borderStyle(BorderStyle.THIN)
                            .borderColor(Color.SMOKY_BLACK)
                            .set();
                }

                for (int markIndex = 0; markIndex < user.getMarks().size(); markIndex++) {
                    var mark = user.getMarks().get(markIndex);

                    worksheet.value(
                            i + 1,
                            markIndex + 3,
                            mark == null ? "не сдано" : mark.getValue()
                    );

                    if (mark == null) {
                        worksheet.style(i + 1, markIndex + 3)
                                .fillColor(Color.RED)
                                .set();
                    } else if (mark.equals(Mark.ZERO)) {
                        worksheet.style(i + 1, markIndex + 3)
                                .fillColor(Color.YELLOW)
                                .set();
                    }

                    worksheet.style(i + 1, markIndex + 3)
                            .borderStyle(BorderStyle.THIN)
                            .borderColor(Color.SMOKY_BLACK)
                            .set();
                }
            }

            book.finish();

            return os.toByteArray();
        }
    }

    @Transactional
    public PracticeMigrationResult migratePractices(UUID fromSemesterId, UUID toSemesterId, List<UUID> groupIds) {
        var alreadyExistedPractices = getPracticesFromSemesterForGroupIds(toSemesterId, groupIds);
        var usersWithoutPractices = getUsersFromGroupsAndWithoutPracticesInSemester(fromSemesterId, groupIds);
        var migratedPractices = migratePractices(fromSemesterId, toSemesterId, groupIds, alreadyExistedPractices);

        return new PracticeMigrationResult(
                migratedPractices.stream()
                        .map(practiceMapper::entityToDto)
                        .toList(),
                alreadyExistedPractices.stream()
                        .map(practiceMapper::entityToDto)
                        .toList(),
                usersWithoutPractices.stream()
                        .map(user -> userMapper.entityToDto(user, userService.getCurrentPractice(user)))
                        .toList()
        );
    }

    private List<PracticeEntity> migratePractices(UUID fromSemesterId,
                                                  UUID toSemesterId,
                                                  List<UUID> groupIds,
                                                  List<PracticeEntity> alreadyExistedPractices
    ) {
        var practicesToMigrate = getPracticesFromSemesterForGroupIdsExcludePracticesForStudentsInList(
                fromSemesterId,
                groupIds,
                alreadyExistedPractices.stream()
                        .map(practice -> practice.getUser()
                                .getId())
                        .toList()
        );

        var newSemester = semesterService.getSemesterEntity(toSemesterId);

        var newPractices = practicesToMigrate.stream().map(practice ->
                PracticeEntity.builder()
                        .comment(practice.getComment())
                        .semester(newSemester)
                        .user(practice.getUser())
                        .company(practice.getCompany())
                        .build()
        ).toList();

        return practiceRepository.saveAll(newPractices);
    }

    private List<PracticeEntity> getPracticesFromSemesterForGroupIds(UUID semesterId, List<UUID> groupIds) {
        Specification<PracticeEntity> spec = Specification
                .where(PracticeSpecification.hasSemester(semesterId))
                .and(PracticeSpecification.userInGroupIds(groupIds));

        return practiceRepository.findAll(spec);
    }

    private List<PracticeEntity> getPracticesFromSemesterForGroupIdsExcludePracticesForStudentsInList(
            UUID semesterId,
            List<UUID> groupIds,
            List<UUID> studentIdsToExclude
    ) {
        Specification<PracticeEntity> spec = Specification
                .where(PracticeSpecification.hasSemester(semesterId))
                .and(PracticeSpecification.userInGroupIds(groupIds))
                .and(PracticeSpecification.userNotIn(studentIdsToExclude));

        return practiceRepository.findAll(spec);
    }

    private List<UserEntity> getUsersFromGroupsAndWithoutPracticesInSemester(UUID semesterId, List<UUID> groupIds) {
        Specification<UserEntity> specification =
                UserSpecification.hasNoPracticeInSemesterAndInGroupIds(semesterId, groupIds);

        return userRepository.findAll(specification);
    }
}
