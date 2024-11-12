package team.weilai.studythrough.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import team.weilai.studythrough.websocket.pojo.model.JsonParse;
import team.weilai.studythrough.websocket.pojo.model.RoleContent;
import team.weilai.studythrough.websocket.pojo.model.Text;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 与大模型建立Socket连接
 *
 * @author gwj
 */
@Slf4j
public class BigModelNew extends WebSocketListener {

    public static final String appid = "3ab592db";

    // 对话历史存储集合
    public static Map<Long,List<RoleContent>> hisMap = new ConcurrentHashMap<>();

    public static String totalAnswer = ""; // 大模型的答案汇总

    private static String newAsk = "";

    public static synchronized void ask(String question) {
        newAsk = question;
    }

    public static final Gson gson = new Gson();

    // 个性化参数
    private Long userId;
    private Boolean wsCloseFlag;

    private static Boolean totalFlag = true; // 控制提示用户是否输入

    // 构造函数
    public BigModelNew(Long userId, Boolean wsCloseFlag) {
        this.userId = userId;
        this.wsCloseFlag = wsCloseFlag;
    }

    //若想要大模型能够根据上下文去回答问题，就要把历史问题和历史回答结果全部传回服务端
    // 由于历史记录最大上线1.2W左右，需要判断是能能加入历史
    public boolean canAddHistory() {
        int len = 0;
        List<RoleContent> list = hisMap.get(userId);
        for (RoleContent temp : list) {
            len = len + temp.getContent().length();
        }
        if (len > 12000) {
            list.remove(0);
            list.remove(1);
            list.remove(2);
            list.remove(3);
            list.remove(4);
            return false;
        } else {
            return true;
        }
    }

    // 线程来发送参数
    class ModelThread extends Thread {
        private WebSocket webSocket;
        private Long userId;

        public ModelThread(WebSocket webSocket, Long userId) {
            this.webSocket = webSocket;
            this.userId = userId;
        }

        public void run() {
            try {
                JSONObject requestJson = new JSONObject();

                JSONObject header = new JSONObject();  // header参数
                header.put("app_id", appid);
                header.put("uid", userId+UUID.randomUUID().toString().substring(0,16));

                JSONObject parameter = new JSONObject(); // parameter参数
                JSONObject chat = new JSONObject();
                chat.put("domain", "4.0Ultra");
                chat.put("temperature", 0.5);
                chat.put("max_tokens", 4096);
                parameter.put("chat", chat);

                JSONObject payload = new JSONObject(); // payload参数
                JSONObject message = new JSONObject();
                JSONArray text = new JSONArray();

                // 历史问题获取
                List<RoleContent> list = hisMap.get(userId);
                if (list != null && !list.isEmpty()) {
                    //log.info("his：{}",list);
                    for (RoleContent tempRoleContent : list) {
                        text.add(JSON.toJSON(tempRoleContent));
                    }
                }

                // 最新问题
                RoleContent roleContent = new RoleContent();
                roleContent.setRole("user");
                roleContent.setContent(newAsk);
                text.add(JSON.toJSON(roleContent));
                hisMap.computeIfAbsent(userId, k -> new ArrayList<>());
                hisMap.get(userId).add(roleContent);

                message.put("text", text);
                payload.put("message", message);

                requestJson.put("header", header);
                requestJson.put("parameter", parameter);
                requestJson.put("payload", payload);
                // System.out.println(requestJson);

                webSocket.send(requestJson.toString());
                // 等待服务端返回完毕后关闭
                while (true) {
                    // System.err.println(wsCloseFlag + "---");
                    Thread.sleep(200);
                    if (wsCloseFlag) {
                        break;
                    }
                }
                webSocket.close(1000, "");
            } catch (Exception e) {
                log.error("【大模型】发送消息错误，{}",e.getMessage());
            }
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.info("上线");
        ModelThread modelThread = new ModelThread(webSocket,userId);
        modelThread.start();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        JsonParse json = gson.fromJson(text, JsonParse.class);
        if (json.getHeader().getCode() != 0) {
            log.error("发生错误，错误码为：{} sid为：{}", json.getHeader().getCode(),json.getHeader().getSid());
            //System.out.println(json);
            webSocket.close(1000, "");
        }
        List<Text> textList = json.getPayload().getChoices().getText();
        int status = json.getHeader().getStatus();
        for (Text temp : textList) {
            // 向客户端发送回答信息，如有存储问答需求，在此处存储
            String msg = temp.getContent();
            if (status == 2) msg += "END";
            ModelChatEndpoint.sendMsgByUserId(userId,msg);

            totalAnswer = totalAnswer + temp.getContent();
        }
        if (status == 2) {
            // 可以关闭连接，释放资源
            if (canAddHistory()) {
                RoleContent roleContent = new RoleContent();
                roleContent.setRole("assistant");
                roleContent.setContent(totalAnswer);
                hisMap.get(userId).add(roleContent);
            } else {
                hisMap.get(userId).remove(0);
                RoleContent roleContent = new RoleContent();
                roleContent.setRole("assistant");
                roleContent.setContent(totalAnswer);
                hisMap.get(userId).add(roleContent);
            }
            wsCloseFlag = true;
            totalFlag = true;
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                System.out.println("onFailure code:" + code);
                System.out.println("onFailure body:" + response.body().string());
                if (101 != code) {
                    System.out.println("connection failed");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // 鉴权方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" + "date: " + date + "\n" + "GET " + url.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        return httpUrl.toString();
    }
}
