package ru.tsu.hits.hitsinternship.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MinioConfiguration {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        var minioClient = MinioClient.builder()
                .endpoint(minioProperties.url())
                .credentials(minioProperties.accessKey(), minioProperties.secretKey())
                .build();

        checkBucketExistence(minioClient);

        return minioClient;
    }

    @SneakyThrows
    private void checkBucketExistence(MinioClient minioClient) {
        var isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.bucket())
                .build());

        if (!isBucketExists) {
            log.info("Bucket {} does not exist. Creating...", minioProperties.bucket());
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.bucket())
                    .build()
            );
            log.info("Bucket {} created", minioProperties.bucket());
        } else {
            log.info("Bucket {} already exists", minioProperties.bucket());
        }
    }

}