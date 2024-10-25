package team.weilai.studythrough.websocket;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import team.weilai.studythrough.mapper.LessonMapper;
import team.weilai.studythrough.mapper.LessonStuMapper;
import team.weilai.studythrough.pojo.Lesson;
import team.weilai.studythrough.pojo.LessonStu;
import team.weilai.studythrough.pojo.Message;
import team.weilai.studythrough.service.MessageService;
import team.weilai.studythrough.websocket.config.GetUserConfigurator;
import team.weilai.studythrough.websocket.pojo.LessonMsg;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gwj
 * @date 2024/10/23 20:34
 */
@ServerEndpoint(value = "/ws/les",configurator = GetUserConfigurator.class)
@Component
@Slf4j
public class LessonEndpoint {

    private static final Map<Long, Set<Session>> map = new ConcurrentHashMap<>();
    private static LessonStuMapper lessonStuMapper;
    private static MessageService messageService;
    private static LessonMapper lessonMapper;
    private Long userId;
    private String username;
    private Integer role;

    @Resource
    private LessonStuMapper mapper;
    @Resource
    private LessonMapper lMapper;
    @Resource
    private MessageService service;

    @PostConstruct
    public void init() {
        lessonStuMapper = mapper;
        messageService = service;
        lessonMapper = lMapper;
    }


    @OnOpen
    public void onOpen(EndpointConfig config,Session session) {
        userId = (Long) config.getUserProperties().get("id");
        username = (String) config.getUserProperties().get("username");
        role = (Integer) config.getUserProperties().get("role");
        if (role == 0) {
            List<LessonStu> list = lessonStuMapper.selectList(new QueryWrapper<LessonStu>()
                    .eq("user_id", userId)
                    .eq("status", 1)
                    .select("lesson_id"));
            for (LessonStu lessonStu : list) {
                add(session, lessonStu.getLessonId());
            }
        }else {
            List<Lesson> list = lessonMapper.selectList(new QueryWrapper<Lesson>()
                    .eq("user_id", userId)
                    .select("lesson_id"));
            for (Lesson lesson : list) {
                add(session, lesson.getLessonId());
            }
        }

    }

    private void add(Session session, Long lessonId) {
        if (map.get(lessonId) == null)  {
            Set<Session> set = new HashSet<>();
            set.add(session);
            map.put(lessonId,set);
        }else {
            map.get(lessonId).add(session);
        }
    }

    @OnMessage
    public void onMessage(Session session,String message) {
        LessonMsg msg = JSONUtil.toBean(message, LessonMsg.class);
        Integer type = msg.getType();
        if (type == 0) {
            msg.setFromUserId(userId);
            msg.setUsername(username);
            msg.setCreateTime(new Date());

            String mess = JSONUtil.toJsonStr(msg);
            Long lessonId = msg.getLessonId();
            Set<Session> set = map.get(lessonId);
            for (Session sess : set) {
                try {
                    sess.getBasicRemote().sendText(mess);
                } catch (IOException e) {
                    log.error("发送消息失败，{}",e.getMessage());
                }
            }
            //存数据库
            Message m = new Message(msg);
            boolean save = messageService.save(m);
            if (!save) {
                log.error("1消息存储数据库失败");
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("连接出错,{}", error.getMessage());
    }

    @OnClose
    public void onClose(Session session) {
        List<LessonStu> list = lessonStuMapper.selectList(new QueryWrapper<LessonStu>()
                .eq("user_id", userId)
                .eq("status", 1)
                .select("lesson_id"));
        for (LessonStu lessonStu : list) {
            Long lessonId = lessonStu.getLessonId();
            boolean remove = map.get(lessonId).remove(session);
            if (!remove) {
                log.error("在[ID]:{}中移除session失败",lessonId);
            }
        }

    }


}
