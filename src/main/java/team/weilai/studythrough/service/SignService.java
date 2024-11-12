package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;
import team.weilai.studythrough.pojo.Sign;
import team.weilai.studythrough.pojo.dto.SignDTO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.websocket.pojo.SignMsg;

/**
 * @author gwj
 * @date 2024/11/7 15:39
 */
public interface SignService extends IService<Sign> {


    Result<SignMsg> pubSign(SignDTO signDTO);

    Result<Void> click(Long signId, String longitude, String latitude);

    @Async
    void add(Long userId,Long signId,Integer status);
}
