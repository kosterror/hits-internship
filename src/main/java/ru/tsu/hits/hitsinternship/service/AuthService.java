package ru.tsu.hits.hitsinternship.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.user.NewUserDto;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailService emailService;
    private final UserRepository userRepository;

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

    private UserEntity findUser(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
    }

}
