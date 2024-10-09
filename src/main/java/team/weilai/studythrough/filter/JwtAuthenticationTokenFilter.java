package team.weilai.studythrough.filter;

import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import team.weilai.studythrough.constants.RedisConstants;
import team.weilai.studythrough.pojo.LoginUser;
import team.weilai.studythrough.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

/**
 * @author gwj
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            //放行，后面的过滤器会判断登录状态
            filterChain.doFilter(request,response);
            return;
        }
        //解析token获取userid
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
            /*DecodedJWT tokenInfo = JwtUtils.getTokenInfo(token);
            Claim claim = tokenInfo.getClaim("userId");
            userId = claim.asString();*/
        } catch (Exception e) {
            log.error("token非法");
            filterChain.doFilter(request,response);
            return;
        }
        //从redis中获取用户信息
        Object o = redisTemplate.opsForValue().get(RedisConstants.LOGIN_USER + userId);
        if (Objects.isNull(o)) {
            log.error("用户未登录");
            filterChain.doFilter(request,response);
            return;
        }
        redisTemplate.expire(RedisConstants.LOGIN_USER+userId, Duration.ofSeconds(RedisConstants.LOGIN_EXPIRE*60));
        LoginUser loginUser = (LoginUser) o;

        //存入SecurityContextHolder
        //存入权限信息
        //使用三个参数的构造方法会将认证状态设成true，方便后面过滤器判断
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        filterChain.doFilter(request,response);
    }
}
