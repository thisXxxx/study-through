package team.weilai.studythrough.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author gwj
 * @date 2024/10/29 20:55
 */
@Data
@Component
@ConfigurationProperties(prefix = "model")
@Configuration
public class BigModelConfig {
    private String hostUrl;
    private String appId;
    private String apiSecret;
    private String apiKey;
}
