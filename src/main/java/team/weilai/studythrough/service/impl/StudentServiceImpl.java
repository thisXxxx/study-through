package team.weilai.studythrough.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.util.enums.StatusCodeEnum;
import team.weilai.studythrough.mapper.LessonStuMapper;
import team.weilai.studythrough.mapper.UserMapper;
import team.weilai.studythrough.pojo.dto.ArgDTO;
import team.weilai.studythrough.pojo.main.Lesson;
import team.weilai.studythrough.pojo.main.LessonStu;
import team.weilai.studythrough.pojo.main.User;
import team.weilai.studythrough.pojo.vo.LessonStuVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.LessonService;
import team.weilai.studythrough.service.StudentService;
import team.weilai.studythrough.util.CommonUtils;
import team.weilai.studythrough.websocket.pojo.AuditStu;

import javax.annotation.Resource;
import java.util.Date;

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
    @Resource
    private UserMapper userMapper;

    @Override
    public Result<AuditStu> join(String code) {
        Lesson lesson = lessonService.query()
                .select("lesson_id", "user_id", "lesson_name", "cover_url")
                .eq("invite_code", code).one();
        if (lesson == null) {
            return Result.fail(StatusCodeEnum.DATA_EMPTY);
        }
        Long userId = CommonUtils.getUserId();
        Long lessonId = lesson.getLessonId();
        LessonStu stu = lessonStuMapper.selectOne(new QueryWrapper<LessonStu>()
                .eq("user_id", userId)
                .eq("lesson_id", lessonId)
                .eq("status", 1));
        if (stu != null) {
            return Result.fail(StatusCodeEnum.DATA_EXIST);
        }

        User user = userMapper.selectOne(new QueryWrapper<User>()
                .select("stu_class", "name")
                .eq("user_id", userId));
        Long teaId = lesson.getUserId();
        String name = user.getName();
        String stuClass = user.getStuClass();
        String lessonName = lesson.getLessonName();
        LessonStu lessonStu = new LessonStu(lessonId, userId, teaId, lessonName, stuClass, name);
        lessonStuMapper.insert(lessonStu);

        AuditStu as = new AuditStu(lessonStu.getLessonStuId(),
                teaId, stuClass, name, lessonName, 0, new Date(), lesson.getCoverUrl(), null);
        return Result.ok(as);
    }

    @Override
    public Result<Page<LessonStuVO>> getList(ArgDTO argDTO) {
        Page<LessonStuVO> page = new Page<>(argDTO.getPageNum(), argDTO.getPageSize());
        Long userId = CommonUtils.getUserId();
        lessonStuMapper.selectLesson(page, argDTO.getName(), userId);
        return Result.ok(page);
    }

    @Override
    public Result<Page<LessonStu>> getRecords(Integer pageNum, Integer pageSize) {
        Page<LessonStu> page = new Page<>(pageNum, pageSize);
        lessonStuMapper.selectPage(page, new QueryWrapper<LessonStu>()
                .select("lesson_name", "status", "update_time")
                .eq("user_id", CommonUtils.getUserId())
                .orderByDesc("update_time")
        );
        return Result.ok(page);
    }
}
