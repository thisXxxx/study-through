package team.weilai.studythrough.websocket;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import team.weilai.studythrough.mapper.LessonStuMapper;
import team.weilai.studythrough.pojo.main.LessonStu;
import team.weilai.studythrough.websocket.config.GetUserConfigurator;
import team.weilai.studythrough.websocket.pojo.AuditStu;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gwj
 * @create 2024/10/15 19:14
 */
@ServerEndpoint(value = "/ws/audit",configurator = GetUserConfigurator.class)
@Component
@Slf4j
public class AuditStuEndpoint {
    private static Map<Long,Session> map = new ConcurrentHashMap<>();
    private Long userId;
    private static LessonStuMapper lessonStuMapper;
    @Resource
    private LessonStuMapper mapper;
    @PostConstruct
    public void init() {
        lessonStuMapper = mapper;
    }

    @OnOpen
    public void onOpen(EndpointConfig endpointConfig,Session session) {
        String string = endpointConfig.getUserProperties().get("id").toString();
        userId = Long.parseLong(string);
        map.put(userId,session);
    }

    @OnMessage
    public void onMessage(String message,Session session) {
        AuditStu bean = JSONUtil.toBean(message, AuditStu.class);
        Long toUser = bean.getToUser();
        Long key = bean.getLessonStuId();
        Long l = 0L;
        if (key == null) {
            l = lessonStuMapper.selectCount(new QueryWrapper<LessonStu>().eq("is_read", 0).eq("user_id", toUser));
        } else {
            l = lessonStuMapper.selectCount(new QueryWrapper<LessonStu>().eq("is_read", 0).eq("tea_id", toUser));
        }
        bean.setNoRead(l);
        Session sess = map.get(toUser);

        if (sess != null) {
            try {
                sess.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("发送消息失败，{}",e.getMessage());
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        map.remove(userId);
        log.info("一用户退出连接，{}",userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("连接出错,{}", error.getMessage());
    }
}
