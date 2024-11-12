package team.weilai.studythrough;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("team.weilai.studythrough.mapper")
public class StudyThroughApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyThroughApplication.class, args);
    }

}
