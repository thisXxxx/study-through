package team.weilai.studythrough.websocket;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.springframework.stereotype.Component;
import team.weilai.studythrough.config.BigModelConfig;
import team.weilai.studythrough.websocket.config.GetUserConfigurator;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 接收客户端请求
 *
 * @author gwj
 * @date 2024/10/29 16:51
 */
@ServerEndpoint(value = "/ws/model", configurator = GetUserConfigurator.class)
@Component
@Slf4j
public class ModelChatEndpoint {
    private static AtomicInteger online = new AtomicInteger(0);
    private static final ConcurrentHashMap<Long,ModelChatEndpoint> wsMap = new ConcurrentHashMap<>();

    private static BigModelConfig config;
    @Resource
    private BigModelConfig modelConfig;

    @PostConstruct
    public void init() {
        config = modelConfig;
    }

    private Session session;
    private Long userId;

    @OnOpen
    public void onOpen(EndpointConfig config, Session session) {
        String s = config.getUserProperties().get("id").toString();
        userId = Long.parseLong(s);
        this.session = session;
        wsMap.put(userId,this);
        online.incrementAndGet();
        log.info("用户{},连接成功，在线人数：{}",userId,online);
    }

    @OnClose
    public void onClose() {
        wsMap.remove(userId);
        online.incrementAndGet();
        log.info("{},退出，在线人数:{}",userId,online);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("连接出错,{}", error.getMessage());
    }

    @OnMessage
    public void onMessage(String message,Session session) throws Exception {
        BigModelNew.ask(message);
        //构建鉴权url
        String authUrl = BigModelNew.getAuthUrl(config.getHostUrl(), config.getApiKey(), config.getApiSecret());
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        WebSocket webSocket = client.newWebSocket(request,
                new BigModelNew(this.userId, false));
        log.info("收到客户端{}的消息：{}", userId, message);
    }


    private void sendMsg(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("客户端{}发送{}失败",userId,message);
        }
    }


    /**
     * 根据userId向用户发送消息
     *
     * @param userId 用户id
     * @param message 消息
     */
    public static void sendMsgByUserId(Long userId,String message) {
        if (userId != null && wsMap.containsKey(userId)) {
            wsMap.get(userId).sendMsg(message);
        }
    }

    /**
     * 关闭Websocket
     *
     * @param userId 用户
     */
    public static void closeWebsocket(Long userId) {
        if (userId != null && wsMap.containsKey(userId)) {
            wsMap.get(userId).onClose();
        }
    }
}
