package team.weilai.studythrough.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.config.MinioConfig;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioClient minioClient;
    @Resource
    private MinioConfig config;
    @Resource(name = "minIOUploadTreadPool")
    private ThreadPoolTaskExecutor poolTaskExecutor;


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
        return config.getEndpoint() + "/" + config.getBucket() + "/" + path;
    }

    /**
     * 删除
     *
     * @param fileName
     */
    public boolean remove(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(config.getBucket()).object(fileName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param fileName    文件名
     * @param inputStream 文件流
     */
    public ObjectWriteResponse uploadFileStream(String bucketName, String fileName, InputStream inputStream) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());
    }


    private static int countDigits(int number) {
        if (number == 0) {
            return 1;  // 0 本身有一位
        }
        int count = 0;
        while (number != 0) {
            number /= 10;
            count++;
        }
        return count;
    }


    /**
     * 通过文件的md5，以及分片文件的索引，构造分片文件的临时存储路径
     *
     * @param md5         文件md5
     * @param currIndex   分片文件索引（从0开始）
     * @param totalPieces 总分片
     * @return 临时存储路径
     */
    public String getFileTempPath(String md5, Integer currIndex, Integer totalPieces) {
        int zeroCnt = countDigits(totalPieces) - countDigits(currIndex);
        StringBuilder name = new StringBuilder(md5);
        name.append("/");
        for (int i = 0; i < zeroCnt; i++) {
            name.append(0);
        }
        name.append(currIndex);
        return name.toString();
    }

    /**
     * 获取路径下文件列表
     *
     * @param bucketName 存储桶
     * @param prefix     文件名称
     * @param recursive  是否递归查找，false：模拟文件夹结构查找
     * @return 二进制流
     */
    public Iterable<Result<Item>> getFilesByPrefix(String bucketName, String prefix,
                                                   boolean recursive) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .recursive(recursive)
                        .build());
    }

    /**
     * 合并分片文件
     *
     * @param bucketName 目标桶
     * @param targetName 完整目标路径
     * @param md5        文件md5
     * @return
     */
    public Integer composePart(String bucketName, String targetName, Integer total, String md5) {
        Iterable<Result<Item>> files = getFilesByPrefix(config.getTempBucket(), md5.concat("/"), false);
        Set<String> savedIndex = Sets.newTreeSet();
        for (Result<Item> item : files) {
            try {
                savedIndex.add(item.get().objectName());
            } catch (Exception e) {
                log.error(e.getMessage());
                return 0;
            }
        }
        if (total != savedIndex.size()) {
            return 2;
        }
        // 文件路径 转 文件合并对象
        List<ComposeSource> sourceObjectList = savedIndex.stream()
                .map(filePath -> ComposeSource.builder()
                        .bucket(config.getTempBucket())
                        .object(filePath)
                        .build())
                .collect(Collectors.toList());
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucketName)
                            .object(targetName)
                            .sources(sourceObjectList)
                            .build());
        } catch (Exception e) {
            log.error("文件合并失败，{}",e.getMessage());
            return 0;
        }
        //异步删除所有临时分片文件
        poolTaskExecutor.execute(() -> {
            List<String> filePaths = Stream.iterate(0, i -> ++i)
                    .limit(total)
                    .map(i -> this.getFileTempPath(md5, i, total))
                    .collect(Collectors.toList());
            Iterable<Result<DeleteError>> deleteResults = this.removeFiles(config.getTempBucket(), filePaths);
            // 遍历错误集合（无元素则成功）
            for (Result<DeleteError> result : deleteResults) {
                DeleteError error = null;
                try {
                    error = result.get();
                } catch (Exception e) {
                    continue;
                }
                log.error("[Bigfile] 分片{}删除失败! 错误信息: {}", error.objectName(), error.message());
            }
        });

        return 1;
    }


    /**
     * 批量删除文件
     *
     * @param bucketName        存储桶
     * @param filePaths<String> 需要删除的文件列表
     * @return Result
     */
    public Iterable<Result<DeleteError>> removeFiles(String bucketName, List<String> filePaths) {
        List<DeleteObject> objectPaths = filePaths.stream()
                .map(DeleteObject::new)
                .collect(Collectors.toList());
        return minioClient.removeObjects(
                RemoveObjectsArgs.builder().bucket(bucketName).objects(objectPaths).build());
    }
}

