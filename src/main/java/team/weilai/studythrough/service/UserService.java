package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.dto.LoginDTO;
import team.weilai.studythrough.pojo.User;
import team.weilai.studythrough.pojo.dto.UserDTO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.pojo.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @author gwj
 * @create 2024/10/9 18:00
 */
public interface UserService extends IService<User> {
    Result<Map<String, String>> login(LoginDTO loginDTO);

    Result<UserVO> getProfile();

    Result<Page<UserVO>> userList(UserDTO userDTO);

    Result<Void> delUsers(List<Long> ids);
}
