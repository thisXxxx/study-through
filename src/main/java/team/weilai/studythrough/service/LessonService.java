package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.DTO.ArgDTO;
import team.weilai.studythrough.pojo.DTO.LessonDTO;
import team.weilai.studythrough.pojo.Lesson;
import team.weilai.studythrough.pojo.VO.LessonVO;
import team.weilai.studythrough.pojo.VO.Result;

/**
 * @author gwj
 * @create 2024/10/10 20:42
 */
public interface LessonService extends IService<Lesson> {
    Result<Void> createLesson(LessonDTO lessonDTO);

    Result<Page<LessonVO>> getList(ArgDTO argDTO);
}
