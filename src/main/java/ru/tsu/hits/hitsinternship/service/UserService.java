package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.PaginationResponse;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;
import ru.tsu.hits.hitsinternship.entity.*;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.CompanyMapper;
import ru.tsu.hits.hitsinternship.mapper.UserMapper;
import ru.tsu.hits.hitsinternship.repository.UserRepository;
import ru.tsu.hits.hitsinternship.specification.UserSpecification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final GroupService groupService;
    private final PasswordEncoder passwordEncoder;
    private final CompanyMapper companyMapper;

    public UserDto getUserById(UUID id) {
        var user = getUserEntityById(id);
        var practice = getCurrentPractice(user);

        return userMapper.entityToDto(user, practice);
    }

    public PracticeEntity getCurrentPractice(UserEntity user) {
        var now = LocalDate.now();

        return user.getPractices()
                .stream()
                .filter(practice ->
                        practice.getSemester() != null
                                && !practice.getSemester().getStartDate().isAfter(now)
                                && !practice.getSemester().getEndDate().isBefore(now))
                .findFirst()
                .orElse(null);
    }

    public UserEntity getUserEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));
    }

    public UserDto updateUserRoles(UUID id, Set<Role> roles) {
        var user = getUserEntityById(id);
        user.setRoles(new ArrayList<>(roles));
        user = userRepository.save(user);
        return userMapper.entityToDto(user, getCurrentPractice(user));
    }

    public PaginationResponse<UserDto> getUsers(String fullName,
                                                Boolean isActive,
                                                List<Role> roles,
                                                List<UUID> groupIds,
                                                int pageNumber,
                                                int pageSize
    ) {
        Specification<UserEntity> spec = Specification.where(null);

        if (fullName != null) {
            spec = spec.and(UserSpecification.hasFullName(fullName));
        }

        if (isActive != null) {
            spec = spec.and(UserSpecification.isActive(isActive));
        }

        if (roles != null && !roles.isEmpty()) {
            spec = spec.and(UserSpecification.hasRoles(roles));
        }

        if (groupIds != null && !groupIds.isEmpty()) {
            spec = spec.and(UserSpecification.inGroupIds(groupIds));
        }

        Sort sort = Sort.by(UserEntity_.GROUP + "." + GroupEntity_.NAME, UserEntity_.FULL_NAME);
        Page<UserEntity> page = userRepository.findAll(spec, PageRequest.of(pageNumber, pageSize, sort));
        List<UserDto> userDtos = page.map(
                user -> userMapper.entityToDto(
                        user,
                        getCurrentPractice(user)
                )
        ).getContent();

        return PaginationResponse.<UserDto>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .elements(userDtos)
                .build();
    }

    public UserDto updateUserGroup(UUID id, UUID groupId) {
        var user = getUserEntityById(id);
        var group = groupService.getGroupEntity(groupId);

        user.setGroup(group);
        user = userRepository.save(user);

        return userMapper.entityToDto(user, getCurrentPractice(user));
    }

    public void changePassword(UUID uuid, String password) {
        var user = getUserEntityById(uuid);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public List<UserEntity> getUsersByGroupIds(List<UUID> groupIds) {
        Specification<UserEntity> spec = UserSpecification.inGroupIds(groupIds);
        return userRepository.findAll(spec, Sort.by(UserEntity_.GROUP + "." + GroupEntity_.NAME, UserEntity_.FULL_NAME));
    }

    public void updateUserStatus(UUID userId, UserStatus userStatus) {
        var user = getUserEntityById(userId);
        user.setStatus(userStatus);
        userRepository.save(user);
    }
}
