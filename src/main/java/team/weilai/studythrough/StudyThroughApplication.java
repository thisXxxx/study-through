package team.weilai.studythrough;

import com.alibaba.fastjson.parser.ParserConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@MapperScan("team.weilai.studythrough.mapper")
@EnableElasticsearchRepositories("team.weilai.studythrough.es.dao")
public class StudyThroughApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyThroughApplication.class, args);
    }

}
