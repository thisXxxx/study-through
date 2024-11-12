package studythrough;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import io.minio.ComposeObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.weilai.studythrough.StudyThroughApplication;
import team.weilai.studythrough.config.BigModelConfig;
import team.weilai.studythrough.util.HaversineUtil;
import team.weilai.studythrough.util.MinioUtil;
import team.weilai.studythrough.websocket.BigModelNew;

import javax.annotation.Resource;
import java.util.Collections;
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
        System.out.println(HaversineUtil.haversine(35.282089, 113.936129, 35.277922, 113.947600));
    }

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/mybatis_plus? characterEncoding=utf-8&userSSL=false", "root", "211322")
                        .globalConfig(builder -> {
                            builder.author("gwj")// 设置作者
                                    .enableSwagger() // 开启 swagger 模式
                                    //.fileOverride() // 覆盖已生成文件
                                    .outputDir("D://Project//studyThrough//src//main//java//team//weilai//studythrough//mapper"); // 指定输出目录
                        })
                        .packageConfig(builder -> {
                            builder.parent("team.weilai") // 设置父包名
                                    .moduleName("studythrough") // 设置父包模块名
                                    .pathInfo(Collections.singletonMap(OutputFile.xml, "D://Project//studyThrough//src//main//resources//mapper"));
// 设置mapperXml生成路径
                        })
                        .strategyConfig(builder -> {
                            builder.addInclude("t_user") // 设置需要生成的表名
                                    .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                        })
                        .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                        .execute();
    }

}
