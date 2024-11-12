package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import team.weilai.studythrough.pojo.exam.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.exam.dto.QuestionDTO;
import team.weilai.studythrough.pojo.exam.dto.QuestionQueryDTO;
import team.weilai.studythrough.pojo.exam.vo.QuestionVO;
import team.weilai.studythrough.pojo.vo.Result;

/**
* @author 86159
* @description 针对表【question】的数据库操作Service
* @createDate 2024-11-12 09:00:32
*/
public interface QuestionService extends IService<Question> {

    @Transactional(rollbackFor = Exception.class)
    Result<Void> add(QuestionDTO questionDTO);

    Result<Page<QuestionVO>> select(QuestionQueryDTO queryDTO);
}
