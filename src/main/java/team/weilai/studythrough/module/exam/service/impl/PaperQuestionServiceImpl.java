package team.weilai.studythrough.module.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import team.weilai.studythrough.mapper.exam.*;
import team.weilai.studythrough.module.exam.service.PaperQuestionService;
import team.weilai.studythrough.pojo.exam.main.*;
import team.weilai.studythrough.util.enums.StatusCodeEnum;
import team.weilai.studythrough.pojo.exam.dto.PaperAnswerDTO;
import team.weilai.studythrough.pojo.exam.vo.PaperDetailVO;
import team.weilai.studythrough.pojo.redis.RedisExam;
import team.weilai.studythrough.pojo.vo.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static team.weilai.studythrough.constants.Constants.NO_CHOOSE;
import static team.weilai.studythrough.constants.RedisConstants.EXAM;

/**
 * @author 86159
 * @description 针对表【paper_question】的数据库操作Service实现
 * @createDate 2024-11-18 21:02:11
 */
@Service
@Slf4j
public class PaperQuestionServiceImpl extends ServiceImpl<PaperQuestionMapper, PaperQuestion>
        implements PaperQuestionService {

    @Resource
    private PaperQuestionMapper baseMapper;
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionAnsMapper questionAnsMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private ExamMapper examMapper;


    @Override
    public Result<PaperDetailVO> detail(Long paperId,boolean isFin) {
        QueryWrapper<PaperQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id",paperId);
        if (!isFin) {
            wrapper.select("paper_qu_id", "question_id", "answered", "question_type");
        }else {
            Paper paper = paperMapper.selectById(paperId);
            Integer status = paper.getStatus();
            if (status == 1) {
                return Result.fail("未交卷");
            } else if (status == 2) {
                return Result.fail("待教师阅卷");
            }
        }
        List<PaperQuestion> list = baseMapper.selectList(wrapper);

        List<PaperQuestion> rl = new ArrayList<>();
        List<PaperQuestion> ml = new ArrayList<>();
        List<PaperQuestion> jl = new ArrayList<>();
        List<PaperQuestion> bl = new ArrayList<>();

        for (PaperQuestion pq : list) {
            Integer type = pq.getQuestionType();
            switch (type) {
                case 0:
                    rl.add(pq);
                    break;
                case 1:
                    ml.add(pq);
                    break;
                case 2:
                    jl.add(pq);
                    break;
                case 3:
                    bl.add(pq);
            }
        }
        Paper paper = paperMapper.selectById(paperId);
        String examName = paper.getExamName();
        PaperDetailVO paperDetailVO = new PaperDetailVO(rl, ml, jl, bl, examName, paper.getKeepTime());
        return Result.ok(paperDetailVO);
    }

    @Override
    public Result<Void> fillAns(PaperAnswerDTO answerDTO) {
        PaperQuestion paperQuestion = baseMapper.selectById(answerDTO.getPaperQuId());
        if (paperQuestion == null) {
            return Result.fail(StatusCodeEnum.DATA_EMPTY);
        }
        paperQuestion.setAnswer(answerDTO.getAnswer());
        paperQuestion.setAnswered(1);
        int i = baseMapper.updateById(paperQuestion);

        return i > 0 ? Result.ok() : Result.fail();
    }


    @Override
    public void markPaper(Long paperId) {
        List<PaperQuestion> list = baseMapper.selectList(new QueryWrapper<PaperQuestion>()
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
            String sa = pq.getStandardAns();
            String standardAns = sa.substring(1, sa.length() - 1);
            if (type == 1) {
                String[] split = answer.split(",");
                List<String> ans = Arrays.asList(standardAns.split(", "));
                for (String string : split) {
                    if (!ans.contains(string)) {
                        pq.setActualScore(0);
                        pq.setIsRight(1);
                        continue loop;
                    }
                }
                if (split.length == ans.size()) {
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
        baseMapper.updateById(list);
        Map<String, Object> map = baseMapper.selectMaps(new QueryWrapper<PaperQuestion>()
                .select("sum(actual_score) as total")
                .eq("paper_id", paperId)).get(0);
        int total = Integer.parseInt(map.get("total").toString());
        UpdateWrapper<Paper> wrapper = new UpdateWrapper<>();
        wrapper.set("obj_score", total).set("user_score", total).eq("paper_id", paperId);
        paperMapper.update(wrapper);
    }

}




