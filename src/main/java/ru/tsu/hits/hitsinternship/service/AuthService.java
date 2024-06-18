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

import java.util.List;
import java.util.UUID;

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

    public void registerStudents(CreateStudentsRequest request) {
        var group = groupService.getGroupEntity(request.getGroupId());
        var newStudentDtos = request.getStudents();

        for (NewStudentDto dto : newStudentDtos) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new ConflictException("Почта %s занята".formatted(dto.getEmail()));
            }

            var user = UserEntity.builder()
                    .fullName(dto.getFullName())
                    .email(dto.getEmail())
                    .isActive(false)
                    .roles(List.of(Role.STUDENT))
                    .group(group)
                    .build();

            userRepository.save(user);
            emailService.sendActivationLink(user);
        }
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
