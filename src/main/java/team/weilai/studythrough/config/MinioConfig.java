package team.weilai.studythrough.config;

import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
@Data
@Slf4j
public class MinioConfig {
    private String url;

    private String access;

    private String secret;

    private String bucket;

    @Bean(name = "minioClient")
    public MinioClient createMinioClient() {
        MinioClient minioClient = null;
        try {// Create a minioClient with the MinIO server playground, its access key and secret key.
            minioClient = new MinioClient(
                    url,access,secret
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return minioClient;
    }

}
