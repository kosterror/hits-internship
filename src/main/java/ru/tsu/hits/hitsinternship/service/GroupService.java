package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.group.GroupDto;
import ru.tsu.hits.hitsinternship.dto.group.NewGroupDto;
import ru.tsu.hits.hitsinternship.entity.GroupEntity;
import ru.tsu.hits.hitsinternship.mapper.GroupMapper;
import ru.tsu.hits.hitsinternship.repository.GroupRepository;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupDto createGroup(NewGroupDto newGroupDto) {

        GroupEntity group = groupMapper.newDtoToEntity(newGroupDto);
        groupRepository.save(group);
        return groupMapper.entityToDto(group);
    }

}
