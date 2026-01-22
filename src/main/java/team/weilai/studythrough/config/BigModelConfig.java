package team.weilai.studythrough.config;

import lombok.Data;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gwj
 * @date 2024/10/29 20:55
 */
@Data
@Component
@ConfigurationProperties(prefix = "model")
@Primary
public class BigModelConfig {
    private List<ModelConfig> models;

    @Data
    public static class ModelConfig {
        private String name;
        private String hostUrl;
        private String appId;
        private String apiSecret;
        private String apiKey;
        private String uploadUrl;
        private String apiPassword;
        private String domain;
    }
}
