package team.weilai.studythrough.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.weilai.studythrough.pojo.LessonStu;
import team.weilai.studythrough.pojo.vo.LessonStuVO;

/**
 * @author gwj
 * @create 2024/10/11 15:16
 */
@Mapper
public interface LessonStuMapper extends BaseMapper<LessonStu> {
    Page<LessonStuVO> selectLesson(@Param("page") Page<LessonStuVO> page,
                                   @Param("name") String name,
                                   @Param("userId")Long userId);

}
