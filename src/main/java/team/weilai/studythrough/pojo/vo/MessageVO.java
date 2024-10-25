package team.weilai.studythrough.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/10/25 17:01
 */
@Data
public class MessageVO {
    private Long messageId;
    private String messageText;
    private String username;
    private Date createTime;
}
