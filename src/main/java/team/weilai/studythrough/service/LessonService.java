package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.dto.ArgDTO;
import team.weilai.studythrough.pojo.dto.LessonDTO;
import team.weilai.studythrough.pojo.Lesson;
import team.weilai.studythrough.pojo.vo.LessonGetVO;
import team.weilai.studythrough.pojo.vo.LessonVO;
import team.weilai.studythrough.pojo.vo.Result;

/**
 * @author gwj
 * @create 2024/10/10 20:42
 */
public interface LessonService extends IService<Lesson> {
    Result<Void> createLesson(LessonDTO lessonDTO);

    Result<Page<LessonVO>> getList(ArgDTO argDTO);

    Result<LessonGetVO> getLesson(Long lessonId);
}
