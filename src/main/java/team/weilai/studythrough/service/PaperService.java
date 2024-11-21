package team.weilai.studythrough.service;

import team.weilai.studythrough.pojo.exam.Paper;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.exam.vo.PaperDetailVO;
import team.weilai.studythrough.pojo.vo.Result;

/**
* @author 86159
* @description 针对表【paper(考试记录)】的数据库操作Service
* @createDate 2024-11-12 21:50:15
*/
public interface PaperService extends IService<Paper> {

    Result<Long> enter(Long examId,Long lessonId);

}
