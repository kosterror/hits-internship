package ru.tsu.hits.hitsinternship.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.hitsinternship.dto.user.UserDto;
import ru.tsu.hits.hitsinternship.entity.PracticeEntity;
import ru.tsu.hits.hitsinternship.entity.UserEntity;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final GroupMapper groupMapper;
    private final PracticeMapper practiceMapper;

    public UserDto entityToDto(UserEntity user, PracticeEntity practice) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setIsActive(user.isActive());
        userDto.setStatus(user.getStatus());
        userDto.setRoles(user.getRoles());
        userDto.setGroup(groupMapper.entityToDto(user.getGroup()));
        userDto.setCurrentPractice(practiceMapper.entityToBaseDto(practice));

        return userDto;
    }


}
