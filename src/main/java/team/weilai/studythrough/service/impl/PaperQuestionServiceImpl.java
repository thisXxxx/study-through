package team.weilai.studythrough.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.weilai.studythrough.mapper.PaperMapper;
import team.weilai.studythrough.pojo.exam.PaperQuestion;
import team.weilai.studythrough.pojo.exam.dto.PaperAnswerDTO;
import team.weilai.studythrough.pojo.exam.vo.PaperDetailVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.PaperQuestionService;
import team.weilai.studythrough.mapper.PaperQuestionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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


    @Override
    public Result<PaperDetailVO> detail(Long paperId) {
        List<PaperQuestion> list = baseMapper.selectList(new QueryWrapper<PaperQuestion>()
                .select("question_id", "answered", "question_type")
                .eq("paper_id", paperId));

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
        String examName = paperMapper.selectById(paperId).getExamName();
        PaperDetailVO paperDetailVO = new PaperDetailVO(rl, ml, jl, bl,examName);
        return Result.ok(paperDetailVO);
    }

    @Override
    public Result<Void> fillAns(PaperAnswerDTO answerDTO) {
        boolean b = update().set("answer", answerDTO.getAnswer()).set("answered", 1)
                .eq("question_id", answerDTO.getQuestionId())
                .eq("paper_id", answerDTO.getPaperId()).update();
        return Result.ok();
    }
}




