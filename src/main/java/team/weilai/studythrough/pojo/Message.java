package team.weilai.studythrough.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.weilai.studythrough.websocket.pojo.LessonMsg;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/10/24 20:18
 */
@Data
@TableName("st_message")
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @TableId(type = IdType.AUTO)
    private Long messageId;
    private String messageText;
    private Long lessonId;
    private Long fromUser;
    private Date createTime;

    public Message(LessonMsg msg) {
        messageText = msg.getMsg();
        lessonId = msg.getLessonId();
        fromUser = msg.getFromUserId();
        createTime = msg.getCreateTime();
    }
}
