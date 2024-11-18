package team.weilai.studythrough.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import team.weilai.studythrough.mapper.*;
import team.weilai.studythrough.pojo.exam.Exam;
import team.weilai.studythrough.pojo.exam.ExamQuestion;
import team.weilai.studythrough.pojo.exam.Paper;
import team.weilai.studythrough.pojo.exam.PaperQuestion;
import team.weilai.studythrough.pojo.redis.RedisExam;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.PaperService;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.util.CommonUtils;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static team.weilai.studythrough.constants.RedisConstants.EXAM;
import static team.weilai.studythrough.constants.RedisConstants.EXAM_QUESTION;

/**
 * @author 86159
 * @description 针对表【paper(考试记录)】的数据库操作Service实现
 * @createDate 2024-11-12 21:50:15
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper>
        implements PaperService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ExamMapper examMapper;
    @Resource
    private ExamQuestionMapper examQuestionMapper;
    @Resource
    private PaperQuestionMapper paperQuestionMapper;


    @Override
    public Result<Void> enter(Long examId, Long lessonId) {
        String key = EXAM + examId;
        Object o = redisTemplate.opsForValue().get(key);
        Date now = new Date();
        if (o == null) {
            Exam exam = examMapper.selectById(examId);
            if (exam == null) {
                return Result.fail("考试不存在");
            }
            Long less = exam.getLessonId();
            if (!less.equals(lessonId)) {
                return Result.fail("非当前课程考试");
            }

            //todo 上锁
            RedisExam redisExam = BeanUtil.copyProperties(exam, RedisExam.class);
            redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisExam));

            o = redisExam;
        }
        RedisExam redisExam = (RedisExam) o;
        Date startTime = redisExam.getStartTime();
        Date endTime = redisExam.getEndTime();
        if (startTime != null && endTime != null) {
            if (startTime.after(now)) {
                return Result.fail("考试暂未开始");
            }
            if (endTime.before(now)) {
                return Result.fail("考试已结束");
            }
        }

        //创建试卷
        Paper paper = BeanUtil.copyProperties(redisExam, Paper.class);
        return createPaper(paper,examId);
    }

    private Result<Void> createPaper(Paper paper,Long examId) {
        paper.setUserId(CommonUtils.getUserId());
        paper.setExamId(examId);
        save(paper);
        Object o = redisTemplate.opsForValue().get(EXAM_QUESTION + examId);
        if (o == null) {
            List<ExamQuestion> examQuestions = examQuestionMapper.selectList(
                    new QueryWrapper<ExamQuestion>().eq("exam_id", examId));
            if (examQuestions == null) {
                return Result.fail("试卷中无题目");
            }
            List<Long> list = examQuestions.stream().map(ExamQuestion::getQuestionId).collect(Collectors.toList());
            redisTemplate.opsForValue().set(EXAM_QUESTION+examId,list);
            o = list;
        }
        List<Long> list = (List<Long>) o;
        List<PaperQuestion> collect = list.stream().map(l -> new PaperQuestion(paper.getPaperId(), l)).collect(Collectors.toList());
        paperQuestionMapper.insert(collect);
        return Result.ok();
    }
}




