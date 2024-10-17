package studythrough;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.weilai.studythrough.StudyThroughApplication;
import team.weilai.studythrough.util.MinioUtil;

import javax.annotation.Resource;

@SpringBootTest(classes = StudyThroughApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudyThroughApplicationTests {

    @Resource
    private MinioUtil minioUtil;

    @Test
    void contextLoads() {
        /*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));*/
        // sdfsa890/05
        System.out.println(minioUtil.getFileTempPath("sdfsa890", 0, 10));
    }

}
