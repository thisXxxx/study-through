package team.weilai.studythrough.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.config.MinioConfig;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MinioUtil {

    private static final ExecutorService executor = new ThreadPoolExecutor(10, 15, 0,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    @Resource
    private MinioConfig minioConfig;
    @Resource
    private MinioClient minioClient;

    public String upFile(MultipartFile file) {
        String type = FileUtil.extName(file.getOriginalFilename());
        String name = UUID.randomUUID().toString(true).substring(0, 20) + StrUtil.DOT + type;
        String path = DateUtil.format(LocalDateTime.now(), "yyyy/MM/") + name;
        try {
            InputStream inputStream = file.getInputStream();
            fileUploader(minioConfig.getBucket(), path, inputStream);
            inputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return minioConfig.getUrl() + "/" + minioConfig.getBucket()+"/" + path;
    }


    /**
     * 文件上传 上传完成后请关闭文件流
     *
     * @param bucket   文件分类 建议文件后缀
     * @param fileName 带后缀的文件名 检验日期yyyyMMdd加16位随机码
     * @param stream   文件流 要上传文件的流
     */
    public void fileUploader(String bucket, String fileName, InputStream stream) throws Exception {
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(bucket.toLowerCase());
        if (!isExist) {
            minioClient.makeBucket(bucket.toLowerCase());
        }
        // 使用putObject上传一个文件到文件分类
        minioClient.putObject(bucket.toLowerCase(), fileName, stream, null, null, null, "application/octet-stream");
    }

    public void frameUp(String bucket,String fileName,InputStream stream) throws Exception {
        boolean isExist = minioClient.bucketExists(bucket.toLowerCase());
        if (!isExist) {
            minioClient.makeBucket(bucket.toLowerCase());
        }
        minioClient.putObject(
                bucket.toLowerCase(),
                fileName,
                stream,
                null,
                null,
                null,
                "image/jpg"
        );
    }

    /**
     * 检查存储桶是否已经存在不存在就创建
     *
     * @param bucket ； 名称
     * @throws Exception : 异常
     */
    public void mkBucket(String bucket) throws Exception {
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(bucket.toLowerCase());
        if (!isExist) {
            minioClient.makeBucket(bucket.toLowerCase());
        }
    }

    /**
     * 判断存储桶是否存在
     *
     * @param bucket :
     * @return :
     */
    @SneakyThrows
    public boolean bucketExists(String bucket) {
        return minioClient.bucketExists(bucket);
    }


    /**
     * 使用putObject上传一个文件到文件分类
     *
     * @param bucket   ： bucket名称
     * @param fileName ： 文件名
     * @param stream   ： 文件流
     * @throws Exception ： 异常
     */
    public void FileUploaderExist(String bucket, String fileName, InputStream stream) throws Exception {
        //mkBucket(bucket);
        minioClient.putObject(bucket.toLowerCase(), fileName, stream, null, null, null, "application/octet-stream");
    }

    /**
     * 文件下载
     *
     * @param bucket   文件分类 建议文件后缀
     * @param fileName 带后缀的文件名 检验日期yyyyMMdd加16位随机码
     */
    public byte[] fileDownloader(String bucket, String fileName) {
        InputStream stream = null;
        try {
            minioClient.statObject(bucket.toLowerCase(), fileName);
            stream = minioClient.getObject(bucket.toLowerCase(), fileName);
            return read(stream);
        } catch (Exception e) {
            return new byte[0];
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 得到文件路径
     *
     * @param bucket   文件分类 建议文件后缀
     * @param fileName 带后缀的文件名 检验日期yyyyMMdd加16位随机码
     */
    public String fileName(String bucket, String fileName) {
        try {
            String minIoUrl = "";
            String url = minioConfig.getUrl();
            if (url.endsWith("/")) {
                minIoUrl = url.substring(0, url.length() - 1);
            }
            minioClient.statObject(bucket.toLowerCase(), fileName);
            return minioClient.presignedGetObject(bucket.toLowerCase(), fileName, 24 * 60 * 60).replace(minIoUrl, "/minio");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除文件
     *
     * @param bucket   文件分类 建议文件后缀
     * @param fileName 带后缀的文件名 检验日期yyyyMMdd加16位随机码
     */
    public Boolean removeFile(String bucket, String fileName) {
        try {
            minioClient.removeObject(bucket.toLowerCase(), fileName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static byte[] read(InputStream inputStream) {

        try (ByteArrayOutputStream bas = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                bas.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            bas.flush();
            return bas.toByteArray();
        } catch (Exception e) {
            log.error("读取文件异常e:{}", e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (Exception er) {
                log.error("关闭文件流失败，{}", er.getMessage());
            }
        }
        return new byte[0];
    }

    @SneakyThrows
    public ObjectStat statObject(String bucketName, String objectName) {
        boolean flag = minioClient.bucketExists(bucketName);
        if (flag) {
            return minioClient.statObject(bucketName, objectName);
        }
        return null;
    }

}

