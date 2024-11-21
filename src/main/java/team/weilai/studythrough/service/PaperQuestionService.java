package team.weilai.studythrough.service;

import team.weilai.studythrough.pojo.exam.PaperQuestion;
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

    Result<PaperDetailVO> detail(Long paperId);

    Result<Void> fillAns(PaperAnswerDTO answerDTO);
}
