package team.weilai.studythrough.config.interceptor;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import team.weilai.studythrough.util.enums.StatusCodeEnum;
import team.weilai.studythrough.mapper.LessonMapper;
import team.weilai.studythrough.mapper.LessonStuMapper;
import team.weilai.studythrough.pojo.main.Lesson;
import team.weilai.studythrough.pojo.main.LessonStu;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author gwj
 * @create 2024/10/14 17:05
 */
@Component
public class LessonInterceptor implements HandlerInterceptor {

    private final LessonStuMapper lessonStuMapper;
    private final LessonMapper lessonMapper;

    public LessonInterceptor(LessonMapper lessonMapper,LessonStuMapper lessonStuMapper) {
        this.lessonStuMapper = lessonStuMapper;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String lessonId = request.getParameter("lessonId");
        Long id = new Long(lessonId);
        Integer role = CommonUtils.getRole();
        Long userId = CommonUtils.getUserId();
        boolean flag;
        if (role == 1) {
            Lesson lesson = lessonMapper.selectById(id);
            flag =  Objects.equals(lesson.getUserId(), userId);
        }else {
            QueryWrapper<LessonStu> wrapper = new QueryWrapper<>();
            wrapper.eq("lesson_id",id).eq("user_id",userId);
            LessonStu lessonStu = lessonStuMapper.selectOne(wrapper);
            flag =  lessonStu != null;
        }
        if (!flag) {
            response.setContentType("application/json; charset=utf-8");
            Result<Object> result = Result.fail(StatusCodeEnum.AUTHORIZED);
            String json = JSONUtil.toJsonStr(result);
            response.getWriter().write(json);
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
