package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.hitsinternship.dto.PaginationResponse;
import ru.tsu.hits.hitsinternship.dto.position.FinalPositionDto;
import ru.tsu.hits.hitsinternship.dto.position.NewPositionDto;
import ru.tsu.hits.hitsinternship.dto.position.PositionDto;
import ru.tsu.hits.hitsinternship.entity.*;
import ru.tsu.hits.hitsinternship.exception.BadRequestException;
import ru.tsu.hits.hitsinternship.exception.ForbiddenException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.PositionMapper;
import ru.tsu.hits.hitsinternship.mapper.UserMapper;
import ru.tsu.hits.hitsinternship.repository.PositionRepository;
import ru.tsu.hits.hitsinternship.specification.PositionSpecification;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    private final PositionMapper positionMapper;

    private final UserMapper userMapper;

    private final CompanyWishesService companyWishesService;

    private final UserService userService;


    public void deletePosition(UUID positionId, UUID userId) {
        checkPermission(userId, positionId);
        if (!positionRepository.existsById(positionId)) {
            throw new NotFoundException("Position with id " + positionId + " not found");
        }
        positionRepository.deleteById(positionId);
    }

    public PositionDto createPosition(NewPositionDto newPositionDto, UUID userId) {

        checkPositionStatus(newPositionDto.getPositionStatus());
        checkOriginality(newPositionDto, userId);
        checkPriority(newPositionDto.getPriority(), userId);
        var company = companyWishesService.findCompanyById(newPositionDto.getCompanyId());
        var speciality = companyWishesService.findSpecialityById(newPositionDto.getSpecialityId());
        var user = userService.getUserEntityById(userId);
        PositionEntity positionEntity = positionMapper.newDtoToEntity(newPositionDto);

        if (newPositionDto.getProgramLanguageId() != null) {
            var programLanguage = companyWishesService.findProgramLanguageById(newPositionDto.getProgramLanguageId());
            positionEntity.setProgramLanguage(programLanguage);
        }
        positionEntity.setCompany(company);
        positionEntity.setSpeciality(speciality);
        positionEntity.setUser(user);
        positionRepository.save(positionEntity);
        return positionMapper.entityToDto(positionEntity);
    }

    public List<PositionDto> getStudentPositions(UUID userId, UUID targetUserId) {

        checkPermissionWithRole(userId, targetUserId);
        var positions = positionRepository.findAllByUserId(userId);
        return positions.stream()
                .map(positionMapper::entityToDto)
                .toList();
    }

    public PositionDto updatePositionStatus(UUID positionId, PositionStatus positionStatus, UUID userId) {

        checkPermission(userId, positionId);
        checkPositionStatus(positionStatus);
        var position = findPositionById(positionId);
        position.setPositionStatus(positionStatus);
        positionRepository.save(position);
        return positionMapper.entityToDto(position);
    }

    public List<PositionDto> updatePositionPriority(List<UUID> positionIdList, UUID userId) {

        checkPosition(userId, positionIdList);
        for (UUID uuid : positionIdList) {
            checkPermission(userId, uuid);
        }
        for (int i = 0; i < positionIdList.size(); i++) {

            var position = findPositionById(positionIdList.get(i));
            position.setPriority(i);
            positionRepository.save(position);
        }
        return positionRepository.findAllByUserId(userId)
                .stream()
                .map(positionMapper::entityToDto)
                .toList();
    }

    public PositionDto confirmReceivedOffer(UUID positionId) {
        var position = findPositionById(positionId);
        position.setPositionStatus(PositionStatus.CONFIRMED_RECEIVED_OFFER);
        positionRepository.save(position);
        return positionMapper.entityToDto(position);
    }

    public void checkPermissionWithRole(UUID userId, UUID targetUserId) {
        var user = userService.getUserEntityById(userId);
        if (user.getRoles().contains(Role.STUDENT) && !targetUserId.equals(userId)) {
            throw new ForbiddenException("You don't have permission to do this");
        }
    }

    private void checkPermission(UUID userId, UUID positionId) {
        var position = findPositionById(positionId);
        if (!position.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You don't have permission to do this");
        }
    }

    private PositionEntity findPositionById(UUID positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new NotFoundException("Position with such id does not exist"));

    }

    private void checkOriginality(NewPositionDto newPositionDto, UUID userId) {

        var positions = positionRepository.findAllByUserId(userId);
        for (var position : positions) {
            if (position.getCompany().getId().equals(newPositionDto.getCompanyId()) &&
                    position.getSpeciality().getId().equals(newPositionDto.getSpecialityId()) &&
                    !checkProgramLanguage(position.getProgramLanguage(), newPositionDto.getProgramLanguageId())
            ) {
                throw new BadRequestException("You already have a position with such parameters");
            }
        }
    }

    private void checkPositionStatus(PositionStatus positionStatus) {
        if (positionStatus == PositionStatus.CONFIRMED_RECEIVED_OFFER) {
            throw new BadRequestException("You can't set this position status");
        }
    }

    private boolean checkProgramLanguage(ProgramLanguageEntity programLanguage, UUID programLanguageId) {

        if (programLanguage != null) {
            return programLanguage.getId().equals(programLanguageId);
        } else return programLanguageId != null;
    }

    private void checkPriority(Integer priority, UUID userId) {
        positionRepository.findAllByUserId(userId).stream()
                .filter(practice -> Objects.equals(practice.getPriority(), priority))
                .findAny()
                .ifPresent(practice -> {
                    throw new BadRequestException("Priority " + priority + " already exists");
                });
    }

    private void checkPosition(UUID userId, List<UUID> positionIdList) {
        if (!Objects.equals(positionRepository.findAllByUserId(userId).size(), positionIdList.size())) {
            throw new BadRequestException("Position list is not correct");
        }
    }

    public PaginationResponse<PositionDto> getPositions(List<UUID> companyIds,
                                                        List<UUID> specialityIds,
                                                        List<UUID> programLanguageIds,
                                                        List<UUID> studentIds,
                                                        List<UUID> groupIds,
                                                        PositionStatus positionStatus,
                                                        int page,
                                                        int size) {
        Specification<PositionEntity> spec = Specification.where(null);

        if (companyIds != null && !companyIds.isEmpty()) {
            spec = spec.and(PositionSpecification.hasCompanyIds(companyIds));
        }

        if (specialityIds != null && !specialityIds.isEmpty()) {
            spec = spec.and(PositionSpecification.hasSpecialityIds(specialityIds));
        }

        if (programLanguageIds != null && !programLanguageIds.isEmpty()) {
            spec = spec.and(PositionSpecification.hasProgramLanguageIds(programLanguageIds));
        }

        if (studentIds != null && !studentIds.isEmpty()) {
            spec = spec.and(PositionSpecification.hasStudentIds(studentIds));
        }

        if (groupIds != null && !groupIds.isEmpty()) {
            spec = spec.and(PositionSpecification.hasGroupIds(groupIds));
        }

        if (positionStatus != null) {
            spec = spec.and(PositionSpecification.hasPositionStatus(positionStatus));
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<PositionEntity> positions = positionRepository.findAll(spec, pageable);

        List<PositionDto> positionDtos = positions.stream()
                .map(positionMapper::entityToDto)
                .toList();

        return PaginationResponse.<PositionDto>builder()
                .pageNumber(positions.getNumber())
                .pageSize(positions.getSize())
                .elements(positionDtos)
                .build();
    }

    @Transactional
    public List<FinalPositionDto> getFinalPositions(List<UUID> groupIds) {
        List<UserEntity> users = userService.getUsersByGroupIds(groupIds);

        return users.stream()
                .map(user -> {
                    List<PositionEntity> positions = positionRepository.findAllByUserId(user.getId());
                    return FinalPositionDto.builder()
                            .student(userMapper.entityToDto(user))
                            .positions(positions.stream()
                                    .filter(p -> p.getPositionStatus() == PositionStatus.ACCEPTED_OFFER)
                                    .map(positionMapper::entityToDto)
                                    .toList())
                            .build();
                })
                .toList();
    }
}
