package team.weilai.studythrough.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.mapper.UserMapper;
import team.weilai.studythrough.pojo.main.LoginUser;
import team.weilai.studythrough.pojo.main.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author gwj
 * @since 2024/5/16 14:31
 */
@Service
public class LoginService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("username","password","user_id","status").eq("username",username);
        User user = userMapper.selectOne(wrapper);
        //如果没有查询到用户信息就抛出异常
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        List<String> list = new ArrayList<>(Arrays.asList(user.getStatus().toString()));

        //把数据封装成UserDetails（接口）返回
        return new LoginUser(user,list);
    }

}
