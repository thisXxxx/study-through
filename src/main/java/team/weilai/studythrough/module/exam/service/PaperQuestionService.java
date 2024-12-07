package team.weilai.studythrough.module.exam.service;

import org.springframework.scheduling.annotation.Async;
import team.weilai.studythrough.pojo.exam.main.PaperQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.exam.dto.PaperAnswerDTO;
import team.weilai.studythrough.pojo.exam.vo.PaperDetailVO;
import team.weilai.studythrough.pojo.vo.Result;

/**
* @author 86159
* @description 针对表【paper_question】的数据库操作Service
* @createDate 2024-11-18 21:02:11
*/
public interface PaperQuestionService extends IService<PaperQuestion> {

    Result<PaperDetailVO> detail(Long paperId,boolean isFin);

    Result<Void> fillAns(PaperAnswerDTO answerDTO);

    @Async
    void markPaper(Long paperId);

}
