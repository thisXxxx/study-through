package team.weilai.studythrough.module.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.weilai.studythrough.pojo.exam.main.QuestionRepo;
import team.weilai.studythrough.module.exam.service.QuestionRepoService;
import team.weilai.studythrough.mapper.exam.QuestionRepoMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【question_repo】的数据库操作Service实现
* @createDate 2024-11-12 08:19:53
*/
@Service
public class QuestionRepoServiceImpl extends ServiceImpl<QuestionRepoMapper, QuestionRepo>
    implements QuestionRepoService{

}




