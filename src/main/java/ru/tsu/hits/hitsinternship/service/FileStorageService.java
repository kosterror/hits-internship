package ru.tsu.hits.hitsinternship.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.hitsinternship.config.MinioProperties;
import ru.tsu.hits.hitsinternship.dto.filemetainfo.FileMetaInfoDto;
import ru.tsu.hits.hitsinternship.entity.FileMetaInfoEntity;
import ru.tsu.hits.hitsinternship.exception.InternalException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.FileMetaInfoMapper;
import ru.tsu.hits.hitsinternship.repository.FileMetaInfoRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileMetaInfoRepository fileMetaInfoRepository;
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final FileMetaInfoMapper fileMetaInfoMapper;
    private final UserService userService;

    public FileMetaInfoDto uploadFile(UUID userId, MultipartFile file) {
        var user = userService.getUserEntityById(userId);

        var fileMetaInfo = FileMetaInfoEntity.builder()
                .id(UUID.randomUUID())
                .bucket(minioProperties.bucket())
                .name(file.getOriginalFilename())
                .size((double) file.getSize() / 1024)
                .owner(user)
                .uploadDateTime(LocalDateTime.now())
                .build();

        var putObjectArgs = getPutObjectArgs(file, fileMetaInfo);
        putObject(putObjectArgs);
        fileMetaInfo = fileMetaInfoRepository.save(fileMetaInfo);

        return fileMetaInfoMapper.toDto(fileMetaInfo);
    }

    public Pair<String, byte[]> downloadFile(UUID fileId) {
        var fileMetaInfo = getFileMetaInfoEntity(fileId);

        var getObjectArgs = GetObjectArgs
                .builder()
                .bucket(fileMetaInfo.getBucket())
                .object(fileMetaInfo.getId().toString())
                .build();

        try (var in = minioClient.getObject(getObjectArgs)) {
            return Pair.of(fileMetaInfo.getName(), in.readAllBytes());
        } catch (Exception exception) {
            throw new InternalException("Error while downloading file", exception);
        }
    }

    public FileMetaInfoDto getFileMetaInfo(UUID fileId) {
        var fileMetaInfo = getFileMetaInfoEntity(fileId);

        return fileMetaInfoMapper.toDto(fileMetaInfo);
    }

    @Transactional
    public List<FileMetaInfoDto> getFileMetaInfoByUserId(UUID userId) {
        var user = userService.getUserEntityById(userId);

        return user.getFiles()
                .stream()
                .map(fileMetaInfoMapper::toDto)
                .toList();
    }

    public List<FileMetaInfoEntity> getFileMetaInfoEntities(List<UUID> fileIds) {
        return fileMetaInfoRepository.findAllById(fileIds);

    }

    private FileMetaInfoEntity getFileMetaInfoEntity(UUID fileId) {
        return fileMetaInfoRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File with id %s not found".formatted(fileId)));
    }

    private PutObjectArgs getPutObjectArgs(MultipartFile file, FileMetaInfoEntity fileMetaInfo) {
        PutObjectArgs putObjectArgs;
        try {
            putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.bucket())
                    .object(fileMetaInfo.getId().toString())
                    .stream(new ByteArrayInputStream(file.getBytes()), file.getSize(), -1)
                    .build();
        } catch (IOException exception) {
            throw new InternalException("Error while reading file", exception);
        }
        return putObjectArgs;
    }

    private void putObject(PutObjectArgs putObjectArgs) {
        try {
            minioClient.putObject(putObjectArgs);
        } catch (Exception exception) {
            throw new InternalException("Error while uploading file", exception);
        }
    }


}
