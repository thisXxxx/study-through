package studythrough;

import io.minio.ComposeObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.weilai.studythrough.StudyThroughApplication;
import team.weilai.studythrough.util.MinioUtil;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest(classes = StudyThroughApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudyThroughApplicationTests {

    @Resource
    private MinioUtil minioUtil;
    @Resource
    private MinioClient minioClient;

    @Test
    void contextLoads() {
        String projectUrl = System.getProperty("user.dir").replaceAll("\\\\", "/");
        System.out.println(projectUrl);
    }

    @Test
    void test() {
        System.out.println(UUID.randomUUID().toString().substring(0,16));
    }

}
