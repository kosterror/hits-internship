package ru.tsu.hits.hitsinternship.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.auth.LoginDto;
import ru.tsu.hits.hitsinternship.dto.user.CreateStudentsRequest;
import ru.tsu.hits.hitsinternship.dto.user.NewStudentDto;
import ru.tsu.hits.hitsinternship.dto.user.NewUserDto;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.exception.UnauthorizedException;
import ru.tsu.hits.hitsinternship.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final GroupService groupService;

    @Transactional
    public void register(NewUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Почта занята");
        }

        var user = UserEntity.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .isActive(false)
                .roles(dto.getRoles())
                .build();

        user = userRepository.save(user);
        emailService.sendActivationLink(user);
    }

    public void activateUser(UUID userId, String password) {
        UserEntity user = findUser(userId);

        if (user.isActive()) {
            throw new ConflictException(String.format("User with id %s is already activated", userId));
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);

        userRepository.save(user);
    }

    public boolean canUserActivateAccount(UUID userId) {
        try {
            UserEntity user = findUser(userId);
            return !user.isActive();
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Transactional
    public void registerStudents(CreateStudentsRequest request) {
        checkEmailsForExisting(request);

        var group = groupService.getGroupEntity(request.getGroupId());
        var newStudentDtos = request.getStudents();

        List<UserEntity> studentEntities = new ArrayList<>(newStudentDtos.size());

        for (NewStudentDto dto : newStudentDtos) {
            var user = UserEntity.builder()
                    .fullName(dto.getFullName())
                    .email(dto.getEmail())
                    .isActive(false)
                    .roles(List.of(Role.STUDENT))
                    .group(group)
                    .build();

            studentEntities.add(user);
        }

        studentEntities = userRepository.saveAll(studentEntities);
        log.info("users saved");

        studentEntities.forEach(student -> {
                    var isSent = emailService.sendActivationLink(student);
                    if (isSent) {
                        log.info("Activation link sent {}", student.getId());
                    } else {
                        log.info("Failed to sent activation link {}", student.getId());
                    }
                }
        );
    }

    private void checkEmailsForExisting(CreateStudentsRequest request) {
        var emails = request.getStudents()
                .stream()
                .map(NewStudentDto::getEmail)
                .collect(Collectors.toSet());

        var existedStudents = userRepository.findAllByEmailIn(emails);

        if (!existedStudents.isEmpty()) {
            throw new ConflictException("Почты: '%s' уже заняты"
                    .formatted(existedStudents.stream()
                            .map(UserEntity::getEmail)
                            .toList()
                    )
            );
        }

        log.info("Users with same emails not found");
    }

    private UserEntity findUser(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));
    }

    public String login(LoginDto loginDto) {
        var user = userRepository
                .findByEmail(loginDto.getEmail())
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with email %s not found", loginDto.getEmail()))
                );

        if (!user.isActive()) {
            throw new ConflictException(String.format("Пользователь с почтой %s не активирован", loginDto.getEmail()));
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            log.error("Incorrect password for user with email {}", loginDto.getEmail());
            throw new UnauthorizedException("Unauthorized");
        }

        return jwtService.generateToken(user);
    }
}
