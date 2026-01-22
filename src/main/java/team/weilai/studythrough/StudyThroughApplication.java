package team.weilai.studythrough;

import com.alibaba.fastjson.parser.ParserConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import team.weilai.studythrough.config.BigModelConfig;

@SpringBootApplication
@MapperScan("team.weilai.studythrough.mapper")
@EnableConfigurationProperties(BigModelConfig.class)
//@EnableElasticsearchRepositories("team.weilai.studythrough.es.dao")
public class StudyThroughApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyThroughApplication.class, args);
    }

}
