package team.weilai.studythrough.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import team.weilai.studythrough.pojo.main.Message;
import team.weilai.studythrough.pojo.vo.MessageVO;

/**
 * @author gwj
 * @date 2024/10/24 20:21
 */
public interface MessageMapper extends BaseMapper<Message> {
    Page<MessageVO> selectChat(@Param("page") Page<MessageVO> page, @Param("lessonId") Long lessonId);
}
