package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.DTO.LoginDTO;
import team.weilai.studythrough.pojo.User;
import team.weilai.studythrough.pojo.VO.Result;

import java.util.Map;

/**
 * @author gwj
 * @create 2024/10/9 18:00
 */
public interface UserService extends IService<User> {
    Result<Map<String, String>> login(LoginDTO loginDTO);

}
