package ru.tsu.hits.hitsinternship.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.auth.LoginDto;
import ru.tsu.hits.hitsinternship.dto.auth.TokensDto;
import ru.tsu.hits.hitsinternship.dto.user.NewStudentDto;
import ru.tsu.hits.hitsinternship.dto.user.NewUserDto;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public void register(NewUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("User with this email already exists");
        }

        var user = UserEntity.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .isActive(false)
                .build();

        user = userRepository.save(user);
        emailService.sendActivationLink(user);
    }

    public void activateUser(UUID userId, String password) {
        UserEntity user = findUser(userId);

        if (Boolean.TRUE.equals(user.getIsActive())) {
            throw new ConflictException("User " + userId + " is already activated");
        }

        user.setPassword(password);
        user.setIsActive(true);

        userRepository.save(user);
    }

    public boolean canUserActivateAccount(UUID userId) {
        try {
            UserEntity user = findUser(userId);
            return !user.getIsActive();
        } catch (NotFoundException e) {
            return false;
        }
    }

    public void registerStudents(List<NewStudentDto> newStudentDtos) {
        for (NewStudentDto dto : newStudentDtos) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new ConflictException("User with this email already exists");
            }

            var user = UserEntity.builder()
                    .fullName(dto.getFullName())
                    .email(dto.getEmail())
                    .isActive(false)
                    .roles(List.of(Role.STUDENT))
                    .build();

            userRepository.save(user);
            emailService.sendActivationLink(user);
        }
    }

    private UserEntity findUser(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
    }

    public TokensDto login(LoginDto loginDto) {
        var user = userRepository
                .findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new NotFoundException("User " + loginDto.getEmail() + " not found"));

        //TODO: проверить пароль и активирован ли аккаунт

        var tokens = jwtService.generateTokens(user);
        user.getRefreshTokens().add(tokens.getRefreshToken());
        userRepository.save(user);

        return tokens;
    }
}
