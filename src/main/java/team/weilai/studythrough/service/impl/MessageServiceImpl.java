package team.weilai.studythrough.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.mapper.MessageMapper;
import team.weilai.studythrough.pojo.Message;
import team.weilai.studythrough.pojo.vo.MessageVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.MessageService;

import javax.annotation.Resource;

/**
 * @author gwj
 * @date 2024/10/24 20:22
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public Result<Page<MessageVO>> chatDetails(Integer pageNum, Integer pageSize, Long lessonId) {
        Page<MessageVO> page = new Page<>(pageNum,pageSize);
        messageMapper.selectChat(page,lessonId);
        return null;
    }
}
