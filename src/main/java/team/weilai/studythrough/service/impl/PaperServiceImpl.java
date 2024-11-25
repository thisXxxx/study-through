package team.weilai.studythrough.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import team.weilai.studythrough.ability.quartz.enums.JobGroup;
import team.weilai.studythrough.ability.quartz.enums.JobPrefix;
import team.weilai.studythrough.ability.quartz.job.BreakExamJob;
import team.weilai.studythrough.ability.quartz.service.JobService;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.mapper.*;
import team.weilai.studythrough.pojo.exam.Exam;
import team.weilai.studythrough.pojo.exam.ExamQuestion;
import team.weilai.studythrough.pojo.exam.Paper;
import team.weilai.studythrough.pojo.exam.PaperQuestion;
import team.weilai.studythrough.pojo.exam.vo.PaperDetailVO;
import team.weilai.studythrough.pojo.redis.RedisExam;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.PaperService;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.util.CommonUtils;
import team.weilai.studythrough.util.CronUtils;

import javax.annotation.Resource;

import java.util.*;
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
    @Resource
    private JobService jobService;
    @Resource
    private PaperMapper paperMapper;


    @Override
    public Result<Long> enter(Long examId, Long lessonId) {
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
            String s = JSONUtil.toJsonStr(redisExam);
            redisTemplate.opsForValue().set(key, s);

            o = s;
        }
        String s = (String) o;
        RedisExam redisExam = JSONUtil.toBean(s, RedisExam.class);
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
        return createPaper(paper, examId);
    }

    @Override
    public Result<Void> handExam(Long paperId) {
        Paper paper = paperMapper.selectById(paperId);
        Integer status = paper.getStatus();
        if (status != 1) {
            return Result.fail(StatusCodeEnum.DATA_EXIST);
        }
        //终止定时任务
        String name = JobPrefix.BREAK_EXAM + paperId;
        jobService.deleteJob(name, JobGroup.SYSTEM);

        //计算考试时长
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());
        long userTime = (System.currentTimeMillis() - paper.getCreateTime().getTime()) / 1000 / 60;
        paper.setUserTime(userTime);

        if (paper.getHasSaq() != 0) {
            paper.setStatus(2);
        } else {
            paper.setStatus(3);
        }

        //异步阅卷
        markPaper(paperId);

        return Result.ok();
    }


    @Override
    public void markPaper(Long paperId) {
        List<PaperQuestion> list = paperQuestionMapper.selectList(new QueryWrapper<PaperQuestion>()
                .eq("paper_id", paperId).in("question_type", 0, 1, 2));
        Paper p = paperMapper.selectById(paperId);
        Long examId = p.getExamId();
        String key = EXAM + examId;
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            Exam exam = examMapper.selectById(examId);

            //todo 上锁
            RedisExam redisExam = BeanUtil.copyProperties(exam, RedisExam.class);
            String s = JSONUtil.toJsonStr(redisExam);
            redisTemplate.opsForValue().set(key, s);

            o = s;
        }
        String s = (String) o;
        RedisExam redisExam = JSONUtil.toBean(s, RedisExam.class);
        Integer radio = redisExam.getRadioScore();
        Integer multi = redisExam.getMultiScore();
        Integer judge = redisExam.getJudgeScore();
        loop:
        for (PaperQuestion pq : list) {
            Integer type = pq.getQuestionType();
            String answer = pq.getAnswer();
            String standardAns = pq.getStandardAns();
            if (type == 1) {
                String[] split = answer.split(",");
                int len = standardAns.length();
                String trim = standardAns.substring(1, len - 1);
                List<String> ans = Arrays.asList(trim.split(", "));
                for (String string : split) {
                    if (!ans.contains(string)) {
                        pq.setActualScore(0);
                        continue loop;
                    }
                }
                if (split.length == len) {
                    pq.setActualScore(multi);
                    pq.setIsRight(0);
                } else {
                    pq.setActualScore(multi / 2);
                    pq.setIsRight(2);
                }
            } else {
                if (Objects.equals(answer, standardAns)) {
                    if (type == 0) {
                        pq.setActualScore(radio);
                    } else {
                        pq.setActualScore(judge);
                    }
                    pq.setIsRight(0);
                } else {
                    pq.setActualScore(0);
                    pq.setIsRight(1);
                }
            }
        }
        paperQuestionMapper.updateById(list);
        PaperQuestion paperQuestion = paperQuestionMapper.selectOne(new QueryWrapper<PaperQuestion>()
                .select("sum(actual_score) as total")
                .eq("paper_id", paperId));
        Integer total = paperQuestion.getTotal();
        update().set("obj_score", total).set("user_score", total).eq("paper_id", paperId).update();
    }

    private Result<Long> createPaper(Paper paper, Long examId) {
        paper.setUserId(CommonUtils.getUserId());
        paper.setExamId(examId);
        save(paper);
        Object o = redisTemplate.opsForValue().get(EXAM_QUESTION + examId);
        if (o == null) {
            List<ExamQuestion> examQuestions = examQuestionMapper.selectList(
                    new QueryWrapper<ExamQuestion>().eq("exam_id", examId).select("question_type", "question_id"));
            if (examQuestions == null) {
                return Result.fail("试卷中无题目");
            }
            redisTemplate.opsForValue().set(EXAM_QUESTION + examId, examQuestions);
            o = examQuestions;
        }
        Long paperId = paper.getPaperId();
        List<ExamQuestion> list = (List<ExamQuestion>) o;
        List<PaperQuestion> collect = list.stream().map(l -> {
            PaperQuestion pq = BeanUtil.copyProperties(l, PaperQuestion.class);
            pq.setPaperId(paperId);
            return pq;
        }).collect(Collectors.toList());
        paperQuestionMapper.insert(collect);

        // 添加强制交卷任务
        Date date = paper.getEndTime();
        Date now = new Date();
        long l = now.getTime() + paper.getKeepTime() * 60 * 1000;
        Date dao = new Date(l);
        if (dao.before(date)) {
            date = dao;
        }
        String jobName = JobPrefix.BREAK_EXAM + paper.getPaperId();
        jobService.addCronJob(BreakExamJob.class, jobName, CronUtils.dateToCron(date), paper.getPaperId() + "");

        return Result.ok(paperId);
    }
}




