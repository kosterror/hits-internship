package ru.tsu.hits.hitsinternship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.hitsinternship.dto.dto.FileMetaInfoDto;
import ru.tsu.hits.hitsinternship.service.FileStorageService;
import ru.tsu.hits.hitsinternship.util.SecurityUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Tag(name = "Файловое хранилище")
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @PreAuthorize("hasAnyRole('STUDENT', 'CURATOR', 'DEAN_OFFICER', 'COMPANY_OFFICER')")
    @Operation(summary = "Загрузить файл в хранилище", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FileMetaInfoDto uploadFile(@RequestParam("file") MultipartFile file) {
        return fileStorageService.uploadFile(SecurityUtil.extractId(), file);
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'CURATOR', 'DEAN_OFFICER', 'COMPANY_OFFICER')")
    @Operation(summary = "Скачать файл", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping(value = "/download/{id}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID id) {
        var nameAndFile = fileStorageService.downloadFile(id);

        var contentDisposition = ContentDisposition.builder("file")
                .filename(nameAndFile.getLeft(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(nameAndFile.getRight()));
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'CURATOR', 'DEAN_OFFICER', 'COMPANY_OFFICER')")
    @Operation(summary = "Получить метаинформацию о своих файлах",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping
    public List<FileMetaInfoDto> getFileMetaInfo() {
        return fileStorageService.getFileMetaInfoByUserId(SecurityUtil.extractId());
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'CURATOR', 'DEAN_OFFICER', 'COMPANY_OFFICER')")
    @Operation(summary = "Получить метаинформацию о файле", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public FileMetaInfoDto getFileMetaInfo(@PathVariable UUID id) {
        return fileStorageService.getFileMetaInfo(id);
    }


}
