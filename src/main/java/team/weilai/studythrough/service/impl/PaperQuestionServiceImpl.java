package team.weilai.studythrough.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.weilai.studythrough.mapper.PaperMapper;
import team.weilai.studythrough.mapper.QuestionAnsMapper;
import team.weilai.studythrough.mapper.QuestionMapper;
import team.weilai.studythrough.pojo.exam.Paper;
import team.weilai.studythrough.pojo.exam.PaperQuestion;
import team.weilai.studythrough.pojo.exam.Question;
import team.weilai.studythrough.pojo.exam.QuestionAns;
import team.weilai.studythrough.pojo.exam.dto.PaperAnswerDTO;
import team.weilai.studythrough.pojo.exam.vo.PaperDetailVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.PaperQuestionService;
import team.weilai.studythrough.mapper.PaperQuestionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static team.weilai.studythrough.constants.Constants.NO_CHOOSE;

/**
 * @author 86159
 * @description 针对表【paper_question】的数据库操作Service实现
 * @createDate 2024-11-18 21:02:11
 */
@Service
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
        paperQuestion.setAnswer(answerDTO.getAnswer());
        paperQuestion.setAnswered(1);
        int i = baseMapper.updateById(paperQuestion);


        new Thread(() -> {
            String standardAns = paperQuestion.getStandardAns();
            if (standardAns == null) {
                Integer type = paperQuestion.getQuestionType();
                Long questionId = paperQuestion.getQuestionId();
                if (Objects.equals(type, NO_CHOOSE)) {
                    Question question = questionMapper.selectById(questionId);
                    paperQuestion.setStandardAns(question.getQuestionAnalysis());
                } else {
                    List<QuestionAns> questionAns = questionAnsMapper.selectList(new QueryWrapper<QuestionAns>()
                            .eq("question_id", questionId)
                            .eq("is_right", 0).select("ans_id"));
                    List<Long> ansIds = questionAns.stream().map(QuestionAns::getAnsId)
                            .collect(Collectors.toList());
                    paperQuestion.setStandardAns(ansIds.toString());
                }
            }
        }).start();


        return i > 0 ? Result.ok() : Result.fail();
    }
}




