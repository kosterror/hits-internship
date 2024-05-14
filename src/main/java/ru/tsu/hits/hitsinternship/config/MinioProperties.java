package ru.tsu.hits.hitsinternship.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioProperties(
        String accessKey,
        String secretKey,
        String bucket,
        String url
) {
}
