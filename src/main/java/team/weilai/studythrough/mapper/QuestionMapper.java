package team.weilai.studythrough.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import team.weilai.studythrough.pojo.exam.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import team.weilai.studythrough.pojo.exam.dto.QuestionQueryDTO;
import team.weilai.studythrough.pojo.exam.vo.PaperQuVO;
import team.weilai.studythrough.pojo.exam.vo.QuCommonVO;
import team.weilai.studythrough.pojo.exam.vo.QuestionDetailVO;
import team.weilai.studythrough.pojo.exam.vo.QuestionVO;

import java.util.List;

/**
* @author 86159
* @description 针对表【question】的数据库操作Mapper
* @createDate 2024-11-12 09:00:32
* @Entity team.weilai.studythrough.pojo.exam.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    Page<QuestionVO> selectQuestion(@Param("page") Page<QuestionVO> page,
                                    @Param("dto") QuestionQueryDTO queryDTO);

    QuestionDetailVO selectDetail(Long questionId);

    List<QuCommonVO> preview(@Param("ids") List<Long> ids);

    PaperQuVO getQu(@Param("questionId") Long questionId,@Param("paperId") Long paperId);
}




