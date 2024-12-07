package team.weilai.studythrough.module.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.weilai.studythrough.mapper.exam.QuestionAnsMapper;
import team.weilai.studythrough.mapper.exam.QuestionRepoMapper;
import team.weilai.studythrough.pojo.exam.main.Question;
import team.weilai.studythrough.pojo.exam.main.QuestionAns;
import team.weilai.studythrough.pojo.exam.main.QuestionRepo;
import team.weilai.studythrough.pojo.exam.dto.QuestionDTO;
import team.weilai.studythrough.pojo.exam.dto.QuestionQueryDTO;
import team.weilai.studythrough.pojo.exam.vo.*;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.mapper.exam.QuestionMapper;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.module.exam.service.QuestionService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static team.weilai.studythrough.constants.Constants.NO_CHOOSE;

/**
* @author 86159
* @description 针对表【question】的数据库操作Service实现
* @createDate 2024-11-12 09:00:32
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

    @Resource
    private QuestionAnsMapper questionAnsMapper;
    @Resource
    private QuestionRepoMapper questionRepoMapper;
    @Resource
    private QuestionMapper questionMapper;


    @Override
    public Result<Void> add(QuestionDTO questionDTO,List<QuestionAns> ansList) {
        Question question = BeanUtil.copyProperties(questionDTO, Question.class);
        save(question);
        Long questionId = question.getQuestionId();
        if (!Objects.equals(question.getQuestionType(), NO_CHOOSE)) {
            if (ansList == null || ansList.isEmpty()) {
                return Result.fail();
            }
            List<QuestionAns> list = ansList.stream().peek(ans ->
                    ans.setQuestionId(questionId)).collect(Collectors.toList());
            questionAnsMapper.insert(list);
        }

        Long repoId = questionDTO.getRepoId();
        if (repoId != null) {
            QuestionRepo repo = new QuestionRepo(questionId, repoId);
            questionRepoMapper.insert(repo);
        }

        return Result.ok();
    }

    @Override
    public Result<Page<QuestionVO>> select(QuestionQueryDTO queryDTO) {
        Page<QuestionVO> page = new Page<>(queryDTO.getPageNum(),queryDTO.getPageSize());
        questionMapper.selectQuestion(page,queryDTO);
        return Result.ok(page);
    }

    @Override
    public Result<QuestionDetailVO> getDetail(Long questionId) {
        QuestionDetailVO questionDetailVO =  questionMapper.selectDetail(questionId);
        return Result.ok(questionDetailVO);
    }

    @Override
    public Result<PaperReviewVO> preview(List<Long> ids) {
        List<QuCommonVO> list = questionMapper.preview(ids);
        List<QuCommonVO> radioList = new ArrayList<>();
        List<QuCommonVO> multiList = new ArrayList<>();
        List<QuCommonVO> judgeList = new ArrayList<>();
        List<QuCommonVO> bigList = new ArrayList<>();
        for (QuCommonVO quCommonVO : list) {
            Integer type = quCommonVO.getQuestionType();
            switch (type) {
                case 0 :
                    radioList.add(quCommonVO);
                    break;
                case 1:
                    multiList.add(quCommonVO);
                    break;
                case 2:
                    judgeList.add(quCommonVO);
                    break;
                case 3:
                    bigList.add(quCommonVO);
            }
        }
        PaperReviewVO reviewVO = new PaperReviewVO(radioList, multiList, judgeList, bigList);
        return Result.ok(reviewVO);
    }

    @Override
    public Result<PaperQuVO> getQu(Long questionId, Long paperId) {
        PaperQuVO qu = questionMapper.getQu(questionId,paperId);
        return Result.ok(qu);
    }
}




