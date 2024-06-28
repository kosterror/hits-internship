package ru.tsu.hits.hitsinternship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.task.MyTaskDto;
import ru.tsu.hits.hitsinternship.dto.task.NewTaskDto;
import ru.tsu.hits.hitsinternship.dto.task.TaskDto;
import ru.tsu.hits.hitsinternship.entity.TaskEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskMapper {

    @Mapping(target = "files", source = "files", ignore = true)
    TaskEntity toEntity(NewTaskDto dto);

    TaskDto toDto(TaskEntity entity);

    MyTaskDto toMyDto(TaskEntity entity);

}
