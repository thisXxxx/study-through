package team.weilai.studythrough.handler;

import cn.hutool.json.JSONUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result<Object> fail = Result.fail(StatusCodeEnum.AUTHORIZED);
        String s = JSONUtil.toJsonStr(fail);
        CommonUtils.renderString(response,s);
    }
}
