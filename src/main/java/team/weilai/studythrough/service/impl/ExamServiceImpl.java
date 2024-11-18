package team.weilai.studythrough.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.mapper.ExamQuestionMapper;
import team.weilai.studythrough.pojo.exam.Exam;
import team.weilai.studythrough.pojo.exam.ExamQuestion;
import team.weilai.studythrough.pojo.exam.dto.ExamDTO;
import team.weilai.studythrough.pojo.redis.RedisExam;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.ExamService;
import team.weilai.studythrough.mapper.ExamMapper;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.util.DateUtil;

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
        List<ExamQuestion> list = ids.stream().map(id -> new ExamQuestion(examId, id))
                .collect(Collectors.toList());
        examQuestionMapper.insert(list);
        redisTemplate.opsForValue().set(EXAM_QUESTION+examId,ids);

        return Result.ok();
    }


    private int getTotal(ExamDTO dto) {
        int big = dto.getBigCnt() * dto.getBigScore();
        int judge = dto.getJudgeCnt() * dto.getJudgeScore();
        int multi = dto.getMultiCnt() * dto.getMultiScore();
        int radio = dto.getRadioCnt() * dto.getRadioScore();
        return big + judge + multi + radio;
    }
}




