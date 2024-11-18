package team.weilai.studythrough.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.mapper.LessonMessageMapper;
import team.weilai.studythrough.pojo.exam.LessonMessage;
import team.weilai.studythrough.service.LessonMessageService;

/**
 * @author gwj
 * @date 2024/11/12 21:30
 */
@Service
public class LessonMessageServiceImpl extends ServiceImpl<LessonMessageMapper, LessonMessage>
        implements LessonMessageService {
}
