package ru.tsu.hits.hitsinternship.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.group.GroupDto;
import ru.tsu.hits.hitsinternship.dto.group.NewGroupDto;
import ru.tsu.hits.hitsinternship.entity.GroupEntity;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.GroupMapper;
import ru.tsu.hits.hitsinternship.repository.GroupRepository;

import java.util.List;
import java.util.UUID;

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

    @Transactional
    public void deleteGroup(UUID groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException(String.format("Group with id %s not found", groupId));
        }
        groupRepository.deleteById(groupId);
    }

    public List<GroupDto> getGroups() {
        return groupRepository.findAll().stream()
                .map(groupMapper::entityToDto)
                .toList();
    }

    public GroupEntity getGroupEntity(UUID groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(String.format("Group with id %s not found", groupId)));
    }

}


