package team.weilai.studythrough.handler;

import cn.hutool.json.JSONUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.util.CommonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gwj
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result<Object> fail = Result.fail(StatusCodeEnum.LOGIN_ERROR);
        String s = JSONUtil.toJsonStr(fail);
        CommonUtils.renderString(response,s);
    }
}
