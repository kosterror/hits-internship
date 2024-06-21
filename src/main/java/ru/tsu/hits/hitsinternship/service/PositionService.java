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
import ru.tsu.hits.hitsinternship.entity.PositionEntity;
import ru.tsu.hits.hitsinternship.entity.PositionStatus;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.exception.BadRequestException;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
import ru.tsu.hits.hitsinternship.exception.ForbiddenException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.PositionMapper;
import ru.tsu.hits.hitsinternship.mapper.UserMapper;
import ru.tsu.hits.hitsinternship.repository.PositionRepository;
import ru.tsu.hits.hitsinternship.specification.PositionSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ru.tsu.hits.hitsinternship.entity.PositionStatus.*;
import static ru.tsu.hits.hitsinternship.entity.UserStatus.GOT_INTERNSHIP;
import static ru.tsu.hits.hitsinternship.entity.UserStatus.IN_SEARCHING;

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
        var position = getPositionForUser(positionId, userId);
        positionRepository.delete(position);
    }

    public PositionDto createPosition(NewPositionDto newPositionDto, UUID userId) {
        if (!isStatusAvailableForCreating(newPositionDto.getPositionStatus())) {
            throw new BadRequestException("This statuses '%s, %s, %s' aren't available for creating new positions"
                    .formatted(CONFIRMED_RECEIVED_OFFER, ACCEPTED_OFFER, REJECTED_OFFER)
            );
        }

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

    public PositionDto updatePositionStatus(UUID positionId,
                                            PositionStatus positionStatus,
                                            UUID userId) {
        var position = getPositionForUser(positionId, userId);
        position.setPositionStatus(positionStatus);

        var isExistPositionWithAcceptedOffer = getStudentPositions(userId, userId)
                .stream()
                .anyMatch(p -> p.getPositionStatus().equals(ACCEPTED_OFFER));

        if (ACCEPTED_OFFER.equals(positionStatus) && isExistPositionWithAcceptedOffer) {
            throw new ConflictException("У вас уже есть принятый оффер, нельзя принять другой");
        }

        if (ACCEPTED_OFFER.equals(positionStatus)) {
            userService.updateUserStatus(userId, GOT_INTERNSHIP);
            position = positionRepository.save(position);
        } else if (isExistPositionWithAcceptedOffer) {
            position = positionRepository.save(position);
        } else {
            position = positionRepository.save(position);
            userService.updateUserStatus(userId, IN_SEARCHING);
        }

        return positionMapper.entityToDto(position);
    }

    private PositionEntity getPositionForUser(UUID positionId, UUID userId) {
        return positionRepository.findByIdAndUserId(positionId, userId)
                .orElseThrow(() -> new NotFoundException(
                                "Position '%s' related to user '%s' not found".formatted(positionId, userId)
                        )
                );
    }

    public List<PositionDto> updatePositionPriority(List<UUID> positionIdList, UUID userId) {
        var positions = new ArrayList<PositionEntity>(positionIdList.size());

        for (int i = 0; i < positionIdList.size(); i++) {
            var positionId = positionIdList.get(i);
            var position = getPositionForUser(positionId, userId);

            position.setPriority(i);

            positions.add(position);
        }

        return positionRepository.saveAll(positions)
                .stream()
                .map(positionMapper::entityToDto)
                .toList();
    }

    public PositionDto confirmReceivedOffer(UUID positionId) {
        var position = findPositionById(positionId);

        position.setPositionStatus(CONFIRMED_RECEIVED_OFFER);

        positionRepository.save(position);
        log.info("Offer for position {} confirmed", positionId);
        return positionMapper.entityToDto(position);
    }

    public void checkPermissionWithRole(UUID userId, UUID targetUserId) {
        var user = userService.getUserEntityById(userId);

        if (user.getRoles().contains(Role.STUDENT)
                && !targetUserId.equals(userId)
                && !user.getRoles().contains(Role.DEAN_OFFICER)) {
            throw new ForbiddenException("You don't have permission to do this");
        }
    }

    private PositionEntity findPositionById(UUID positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new NotFoundException("Position with such id does not exist"));

    }

    private boolean isStatusAvailableForCreating(PositionStatus positionStatus) {
        return !CONFIRMED_RECEIVED_OFFER.equals(positionStatus)
                && !ACCEPTED_OFFER.equals(positionStatus)
                && !REJECTED_OFFER.equals(positionStatus);
    }

    private void checkPriority(Integer priority, UUID userId) {
        positionRepository.findAllByUserId(userId)
                .stream()
                .filter(practice -> Objects.equals(practice.getPriority(), priority))
                .findAny()
                .ifPresent(practice -> {
                    throw new BadRequestException("Priority " + priority + " already exists");
                });
    }

    public PaginationResponse<PositionDto> getPositions(List<UUID> companyIds,
                                                        List<UUID> specialityIds,
                                                        List<UUID> programLanguageIds,
                                                        String fullName,
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

        if (fullName != null && !fullName.isEmpty()) {
            spec = spec.and(PositionSpecification.fullNameLike(fullName));
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
                            .student(userMapper.entityToDto(user, userService.getCurrentPractice(user)))
                            .positions(positions.stream()
                                    .filter(p -> p.getPositionStatus() == ACCEPTED_OFFER)
                                    .map(positionMapper::entityToDto)
                                    .toList())
                            .build();
                })
                .toList();
    }
}
