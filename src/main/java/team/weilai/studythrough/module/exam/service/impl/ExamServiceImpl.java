package team.weilai.studythrough.module.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import team.weilai.studythrough.mapper.exam.ExamMapper;
import team.weilai.studythrough.mapper.exam.ExamQuestionMapper;
import team.weilai.studythrough.mapper.exam.PaperMapper;
import team.weilai.studythrough.mapper.exam.QuestionMapper;
import team.weilai.studythrough.module.exam.service.ExamService;
import team.weilai.studythrough.util.enums.StatusCodeEnum;
import team.weilai.studythrough.mapper.*;
import team.weilai.studythrough.pojo.main.LessonStu;
import team.weilai.studythrough.pojo.main.User;
import team.weilai.studythrough.pojo.exam.main.Exam;
import team.weilai.studythrough.pojo.exam.main.ExamQuestion;
import team.weilai.studythrough.pojo.exam.main.Paper;
import team.weilai.studythrough.pojo.exam.dto.ExamDTO;
import team.weilai.studythrough.pojo.exam.dto.ExamQueryDTO;
import team.weilai.studythrough.pojo.exam.dto.NoExam;
import team.weilai.studythrough.pojo.redis.RedisExam;
import team.weilai.studythrough.pojo.vo.Result;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.util.CommonUtils;
import team.weilai.studythrough.util.DateUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static team.weilai.studythrough.constants.RedisConstants.EXAM;
import static team.weilai.studythrough.constants.RedisConstants.EXAM_QUESTION;

/**
 * @author 86159
 * @description 针对表【exam】的数据库操作Service实现
 * @createDate 2024-11-12 19:54:00
 */
@Service
@Slf4j
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam>
        implements ExamService {

    @Resource
    private ExamQuestionMapper examQuestionMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private LessonStuMapper lessonStuMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result<Void> add(ExamDTO dto) {
        String startTime = dto.getStartTime();
        String endTime = dto.getEndTime();
        Exam exam = BeanUtil.copyProperties(dto, Exam.class);
        Date start = null;
        Date end = null;
        if (StrUtil.isNotBlank(startTime) && StrUtil.isNotBlank(endTime)) {
            try {
                start = DateUtil.parseString(startTime);
                end = DateUtil.parseString(endTime);
                if (start.after(end)) {
                    throw new Exception("时间错误");
                }
                long between = DateUtil.between(start, end);
                if (between < dto.getKeepTime()) {
                    throw new Exception("时间错误");
                }
                exam.setStartTime(start);
                exam.setEndTime(end);
            } catch (Exception e) {
                return Result.fail(StatusCodeEnum.VALID_ERROR);
            }
        }


        save(exam);
        Long examId = exam.getExamId();
        RedisExam redisExam = BeanUtil.copyProperties(exam, RedisExam.class);

        redisTemplate.opsForValue().set(EXAM + examId, JSONUtil.toJsonStr(redisExam));

        List<Long> ids = dto.getQuestionIds();
        List<ExamQuestion> list = ids.stream().map(id -> {
                    Integer questionType = questionMapper.selectById(id).getQuestionType();
                    return new ExamQuestion(examId, id, questionType);
                })
                .collect(Collectors.toList());
        examQuestionMapper.insert(list);
        redisTemplate.opsForValue().set(EXAM_QUESTION + examId, list);

        return Result.ok();
    }

    @Override
    public Result<Page<Exam>> getExam(ExamQueryDTO queryDTO) {
        Integer role = CommonUtils.getRole();
        String examName = queryDTO.getExamName();
        boolean b = examName != null && !examName.isEmpty();
        Page<Exam> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        query().like(b, "exam_name", examName).eq("lesson_id", queryDTO.getLessonId())
                .page(page);
        if (role != 0) {
            return Result.ok(page);
        }
        Long userId = CommonUtils.getUserId();
        List<Exam> records = page.getRecords();
        for (Exam record : records) {
            Long examId = record.getExamId();
            Paper paper = paperMapper.selectOne(new QueryWrapper<Paper>()
                    .select("status")
                    .eq("user_id", userId).eq("exam_id", examId));
            if (paper == null || paper.getStatus() == 1) {
                record.setHasFin(0);
            } else {
                record.setHasFin(paper.getStatus());
            }
        }
        page.setRecords(records);

        return Result.ok(page);
    }

    @Override
    public Result<List<NoExam>> getPart(Long examId) {
        Exam exam = query().eq("exam_id", examId).select("lesson_id").one();
        if(exam == null) {
            return Result.fail(StatusCodeEnum.DATA_EMPTY);
        }
        List<Paper> papers = paperMapper.selectList(new QueryWrapper<Paper>().eq("exam_id", examId).select("user_id"));
        List<Long> partList = papers.stream().map(Paper::getPaperId).collect(Collectors.toList());
        List<LessonStu> list = lessonStuMapper.selectList(new QueryWrapper<LessonStu>()
                .notIn("user_id", partList).select("user_id")
                .eq("lesson_id", exam.getLessonId()));
        List<Long> ids = list.stream().map(LessonStu::getUserId).collect(Collectors.toList());
        List<User> users = userMapper.selectList(new QueryWrapper<User>().in("user_id", ids).select("user_id", "name"));
        List<NoExam> collect = users.stream().map(u -> new NoExam(u.getName(), u.getUserId())).collect(Collectors.toList());
        return Result.ok(collect);
    }

}




