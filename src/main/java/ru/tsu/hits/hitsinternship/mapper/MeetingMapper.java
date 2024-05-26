package ru.tsu.hits.hitsinternship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.meeting.MeetingDto;
import ru.tsu.hits.hitsinternship.dto.meeting.NewMeetingDto;
import ru.tsu.hits.hitsinternship.entity.MeetingEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MeetingMapper {

    MeetingEntity toEntity(NewMeetingDto dto);

    MeetingDto toDto(MeetingEntity entity);

}
