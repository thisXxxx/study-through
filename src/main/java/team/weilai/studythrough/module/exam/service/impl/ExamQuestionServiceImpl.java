package team.weilai.studythrough.module.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.weilai.studythrough.pojo.exam.main.ExamQuestion;
import team.weilai.studythrough.module.exam.service.ExamQuestionService;
import team.weilai.studythrough.mapper.exam.ExamQuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【exam_question(试卷题目关联表)】的数据库操作Service实现
* @createDate 2024-11-12 19:57:42
*/
@Service
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionMapper, ExamQuestion>
    implements ExamQuestionService{

}




