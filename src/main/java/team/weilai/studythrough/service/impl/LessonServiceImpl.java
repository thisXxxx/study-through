package team.weilai.studythrough.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.mapper.DocMapper;
import team.weilai.studythrough.mapper.LessonMapper;
import team.weilai.studythrough.pojo.dto.ArgDTO;
import team.weilai.studythrough.pojo.dto.LessonDTO;
import team.weilai.studythrough.pojo.Doc;
import team.weilai.studythrough.pojo.Lesson;
import team.weilai.studythrough.pojo.vo.LessonVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.LessonService;
import team.weilai.studythrough.util.CommonUtils;
import team.weilai.studythrough.util.DateUtil;
import team.weilai.studythrough.util.MinioUtil;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;

import static team.weilai.studythrough.constants.Constants.SOURCE;

/**
 * @author gwj
 * @create 2024/10/10 20:43
 */
@Service
@Slf4j
public class LessonServiceImpl extends ServiceImpl<LessonMapper, Lesson>
        implements LessonService {

    @Resource
    private MinioUtil minioUtil;
    @Resource
    private LessonMapper lessonMapper;
    @Resource
    private DocMapper docMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> createLesson(LessonDTO lessonDTO) {
        Lesson lesson = null;
        try {
            Date date = DateUtil.parseString(lessonDTO.getEnd());
            lesson = new Lesson();
            lesson.setLessonName(lessonDTO.getLessonName());
            lesson.setEndTime(date);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return Result.fail(StatusCodeEnum.VALID_ERROR);
        }
        lesson.setUserId(CommonUtils.getUserId());
        MultipartFile file = lessonDTO.getFile();
        String cover = minioUtil.upload(file);
        lesson.setCoverUrl(cover);
        lesson.setInviteCode(RandomUtil.randomString(SOURCE,12));
        save(lesson);
        Doc doc = new Doc(lesson.getLessonId());
        docMapper.insert(doc);
        return Result.ok();
    }

    @Override
    public Result<Page<LessonVO>> getList(ArgDTO argDTO) {
        Page<LessonVO> page = new Page<>(argDTO.getPageNum(), argDTO.getPageSize());
        lessonMapper.selectLesson(page,argDTO.getName());
        return Result.ok(page);
    }
}
