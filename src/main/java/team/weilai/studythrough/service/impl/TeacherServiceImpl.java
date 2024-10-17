package team.weilai.studythrough.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.config.MinioConfig;
import team.weilai.studythrough.mapper.LessonStuMapper;
import team.weilai.studythrough.pojo.LessonStu;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.TeacherService;
import team.weilai.studythrough.util.CommonUtils;
import team.weilai.studythrough.util.MinioUtil;
import team.weilai.studythrough.websocket.pojo.AuditStu;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

import static team.weilai.studythrough.constants.Constants.PATH_PREFIX;
import static team.weilai.studythrough.constants.RedisConstants.*;

/**
 * @author gwj
 * @create 2024/10/16 19:31
 */
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    @Resource
    private LessonStuMapper lessonStuMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private MinioUtil minioUtil;
    @Resource
    private MinioConfig minioConfig;

    @Override
    public Result<Page<LessonStu>> auditList(Integer pageNum, Integer pageSize) {
        Page<LessonStu> page = new Page<>(pageNum, pageSize);
        QueryWrapper<LessonStu> wrapper = new QueryWrapper<>();
        wrapper.select("lesson_stu_id,lesson_name,stu_class,name,create_time,status")
                .eq("tea_id", CommonUtils.getUserId());
        lessonStuMapper.selectPage(page, wrapper);
        return Result.ok(page);
    }

    @Override
    public Result<AuditStu> audit(Long lessonStuId, Integer choose) {
        LessonStu lessonStu = lessonStuMapper.selectOne(new QueryWrapper<LessonStu>()
                .select("user_id","lesson_name")
                .eq("lesson_stu_id", lessonStuId).eq("status", 0));
        if (lessonStu == null) {
            return Result.fail();
        }
        Date date = new Date();
        lessonStuMapper.update(new UpdateWrapper<LessonStu>()
                .set("status", choose).set("update_time", date)
                .eq("lesson_stu_id", lessonStuId));
        AuditStu as = new AuditStu(lessonStu.getUserId(),lessonStu.getLessonName(),choose, date);
        return Result.ok(as);
    }

    @Override
    public Result<String> uploadBig(MultipartFile file, Integer chunk, Integer total, String md5) {
        //校验md5文件是否已经上传
        Object o = redisTemplate.opsForHash().get(MD5 + md5 + OVER, "url");
        if (o != null) {
            String url = (String) o;
            return Result.ok(url, "文件已上传成功");
        }
        //校验分片是否已经上传
        Boolean flag = redisTemplate.opsForSet().isMember(MD5 + md5 + CHUNKS, chunk);
        if (Boolean.TRUE.equals(flag)) {
            return Result.ok(null, "分片已经上传过");
        }

        //上传分片
        String tempPath = minioUtil.getFileTempPath(md5, chunk, total);
        try {
            minioUtil.uploadFileStream(minioConfig.getTempBucket(),tempPath,file.getInputStream());
        } catch (Exception e) {
            log.error("分片上传出错，{}",e.getMessage());
            return Result.fail();
        }
        log.info("分片{}上传成功", chunk);
        redisTemplate.opsForSet().add(MD5 + md5 + CHUNKS, chunk);
        //redisTemplate.expire(MD5 + md5 + CHUNKS, 6, TimeUnit.HOURS);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("url", null);
        redisTemplate.opsForHash().putAll(MD5 + md5 + OVER, map);
        return Result.ok();
    }

    @Override
    public Result<String> mergePart(String md5, String fileName,Integer total) {
        //检查文件是否已经上传过
        Object url = redisTemplate.opsForHash().get(MD5 + md5 + OVER, "url");
        if (url != null) {
            return Result.ok((String) url);
        }
        String type = FileUtil.extName(fileName);
        String name = UUID.randomUUID().toString(true).substring(0, 20) + StrUtil.DOT + type;
        String target = DateUtil.format(LocalDateTime.now(), "yyyy/MM/") + name;
        //合并切片
        String bucket = minioConfig.getBucket();
        Integer res = minioUtil.composePart(bucket, target, total, md5);
        if (res == 0) {
            return Result.fail();
        }
        if (res == 2) {
            return Result.fail("分片未全部上传");
        }
        String path = PATH_PREFIX + bucket + "/"+target;
        redisTemplate.opsForHash().put(MD5 + md5 + OVER, "url", path);
        return Result.ok(path);
    }

    @Override
    public Result<List<Integer>> getNoUp(String md5) {
        Object url = redisTemplate.opsForHash().get(MD5 + md5 + OVER, "url");
        if (url != null) {
            return Result.ok(null, "已经上传成功");
        }
        Object o = redisTemplate.opsForHash().get(MD5 + md5 + OVER, "total");
        if (o == null) {
            return Result.fail("未找到");
        }
        int total = (Integer) o;
        List<Integer> remainIndex = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            Boolean is = redisTemplate.opsForSet().isMember(MD5 + md5 + CHUNKS, i);
            if (Boolean.FALSE.equals(is)) {
                remainIndex.add(i);
            }
        }
        return Result.ok(remainIndex);
    }
}
