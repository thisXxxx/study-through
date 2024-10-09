package team.weilai.studythrough.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.mapper.UserMapper;
import team.weilai.studythrough.pojo.DTO.LoginDTO;
import team.weilai.studythrough.pojo.LoginUser;
import team.weilai.studythrough.pojo.User;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.UserService;
import team.weilai.studythrough.util.JwtUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static team.weilai.studythrough.constants.RedisConstants.LOGIN_EXPIRE;
import static team.weilai.studythrough.constants.RedisConstants.LOGIN_USER;

/**
 * @author gwj
 * @create 2024/10/9 18:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public Result<Map<String, String>> login(LoginDTO loginDTO) {
        String password = loginDTO.getPassword();
        String username = loginDTO.getUsername();
        //调用authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //用户信息存入上下文
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        LoginUser principal = (LoginUser) authenticate.getPrincipal();
        Long id = principal.getUser().getUserId();
        Integer status = principal.getUser().getStatus();
        HashMap<String, String> map = new HashMap<>();
        String token = JwtUtil.createJWT(id.toString());
        map.put("role",status.toString());
        map.put("token",token);
        redisTemplate.opsForValue().set(LOGIN_USER+id,id,LOGIN_EXPIRE, TimeUnit.MINUTES);
        return Result.ok(map);
    }
}
