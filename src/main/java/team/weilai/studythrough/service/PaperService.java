package team.weilai.studythrough.service;

import org.springframework.scheduling.annotation.Async;
import team.weilai.studythrough.pojo.exam.Paper;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.exam.PaperQuestion;
import team.weilai.studythrough.pojo.exam.vo.PaperDetailVO;
import team.weilai.studythrough.pojo.vo.Result;

import java.util.List;

/**
* @author 86159
* @description 针对表【paper(考试记录)】的数据库操作Service
* @createDate 2024-11-12 21:50:15
*/
public interface PaperService extends IService<Paper> {

    Result<Long> enter(Long examId,Long lessonId);

    Result<Void> handExam(Long paperId);

    @Async
    void markPaper(Long paperId);
}
