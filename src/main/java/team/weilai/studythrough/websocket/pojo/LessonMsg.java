package team.weilai.studythrough.websocket.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/10/23 20:56
 */
@Data
public class LessonMsg {
    private Long lessonId;
    private Long fromUserId;
    private String username;
    private String msg;
    private Long noRead;
    /**
     * 0：聊天
     */
    private Integer type;
    private Date createTime;
}
