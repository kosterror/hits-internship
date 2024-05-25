package ru.tsu.hits.hitsinternship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.filemetainfo.FileMetaInfoDto;
import ru.tsu.hits.hitsinternship.entity.FileMetaInfoEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FileMetaInfoMapper {

    FileMetaInfoDto toDto(FileMetaInfoEntity entity);

}
