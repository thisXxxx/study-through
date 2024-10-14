package team.weilai.studythrough.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.config.MinioConfig;

import javax.annotation.Resource;
import java.time.LocalDateTime;


@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioClient minioClient;
    @Resource
    private MinioConfig config;


    /**
     * 单文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file) {
        String type = FileUtil.extName(file.getOriginalFilename());
        String name = UUID.randomUUID().toString(true).substring(0, 20) + StrUtil.DOT + type;
        String path = DateUtil.format(LocalDateTime.now(), "yyyy/MM/") + name;
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(config.getBucket()).object(path)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return config.getEndpoint()+"/"+config.getBucket()+"/"+path;
    }

    /**
     * 删除
     *
     * @param  fileName
     */
    public boolean remove(String fileName){
        try {
            minioClient.removeObject( RemoveObjectArgs.builder().bucket(config.getBucket()).object(fileName).build());
        }catch (Exception e){
            return false;
        }
        return true;
    }

}

