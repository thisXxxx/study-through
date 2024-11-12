package team.weilai.studythrough.websocket.pojo.model;

import lombok.Data;
import lombok.ToString;

/**
 * 返回的json结果拆解
 *
 * @author gwj
 * @date 2024/10/29 21:06
 */
@Data
@ToString
public class JsonParse {
    Header header;
    Payload payload;
}

