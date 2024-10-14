package team.weilai.studythrough.util;

/**
 * @author gwj
 */
import io.swagger.models.auth.In;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import team.weilai.studythrough.pojo.LoginUser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommonUtils {
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        return loginUser.getUser().getUserId();
    }

    public static Integer getRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        return loginUser.getUser().getStatus();
    }

}
