package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.main.Message;
import team.weilai.studythrough.pojo.vo.MessageVO;
import team.weilai.studythrough.pojo.vo.Result;

/**
 * @author gwj
 * @date 2024/10/24 20:22
 */
public interface MessageService extends IService<Message> {
    Result<Page<MessageVO>> chatDetails(Integer pageNum, Integer pageSize, Long lessonId);
}
