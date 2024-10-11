package team.weilai.studythrough.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.mapper.LessonStuMapper;
import team.weilai.studythrough.pojo.DTO.ArgDTO;
import team.weilai.studythrough.pojo.Lesson;
import team.weilai.studythrough.pojo.LessonStu;
import team.weilai.studythrough.pojo.VO.LessonStuVO;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.LessonService;
import team.weilai.studythrough.service.StudentService;
import team.weilai.studythrough.util.CommonUtils;

import javax.annotation.Resource;

/**
 * @author gwj
 * @create 2024/10/11 15:08
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private LessonService lessonService;
    @Resource
    private LessonStuMapper lessonStuMapper;

    @Override
    public Result<Void> join(String code) {
        Lesson lesson = lessonService.query()
                .select("lesson_id")
                .eq("invite_code", code).one();
        if (lesson == null) {
            return Result.fail(StatusCodeEnum.DATA_EMPTY);
        }
        LessonStu lessonStu = new LessonStu(lesson.getLessonId(), CommonUtils.getUserId());
        lessonStuMapper.insert(lessonStu);
        return Result.ok();
    }

    @Override
    public Result<Page<LessonStuVO>> getList(ArgDTO argDTO) {
        Page<LessonStuVO> page = new Page<>(argDTO.getPageNum(),argDTO.getPageSize());
        Long userId = CommonUtils.getUserId();
        lessonStuMapper.selectLesson(page,argDTO.getName(),userId);
        return Result.ok(page);
    }
}
