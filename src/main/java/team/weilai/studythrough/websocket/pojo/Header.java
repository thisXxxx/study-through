package team.weilai.studythrough.websocket.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @author gwj
 * @date 2024/10/29 21:12
 */
@Data
@ToString
public class Header {
    int code;
    int status;
    String sid;
}
