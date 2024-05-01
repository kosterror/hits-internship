package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.entity.UserEntity;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.UserMapper;
import ru.tsu.hits.hitsinternship.repository.UserRepository;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUserById(UUID id) {
        var user = getUserEntityById(id);
        return userMapper.entityToDto(user);
    }

    public UserEntity getUserEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));
    }

    public UserDto updateUserRoles(UUID id, Set<Role> roles) {
        var user = getUserEntityById(id);
        user.setRoles(new ArrayList<>(roles));
        user = userRepository.save(user);
        return userMapper.entityToDto(user);
    }
}
