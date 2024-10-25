package team.weilai.studythrough.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.weilai.studythrough.pojo.Lesson;
import team.weilai.studythrough.pojo.vo.LessonVO;

/**
 * @author gwj
 * @create 2024/10/10 20:42
 */
@Mapper
public interface LessonMapper extends BaseMapper<Lesson> {
    Page<LessonVO> selectLesson(@Param("page") Page<LessonVO> page,
                                @Param("name") String name);
}
