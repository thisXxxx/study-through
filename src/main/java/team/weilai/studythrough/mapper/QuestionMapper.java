package team.weilai.studythrough.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import team.weilai.studythrough.pojo.exam.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import team.weilai.studythrough.pojo.exam.dto.QuestionQueryDTO;
import team.weilai.studythrough.pojo.exam.vo.QuestionVO;

/**
* @author 86159
* @description 针对表【question】的数据库操作Mapper
* @createDate 2024-11-12 09:00:32
* @Entity team.weilai.studythrough.pojo.exam.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    Page<QuestionVO> selectQuestion(@Param("page") Page<QuestionVO> page,
                                    @Param("dto") QuestionQueryDTO queryDTO);
}




