package team.weilai.studythrough.websocket.pojo.model;

import lombok.Data;
import lombok.ToString;
import team.weilai.studythrough.websocket.pojo.model.Choices;

/**
 * @author gwj
 * @date 2024/10/29 21:13
 */
@Data
@ToString
public class Payload {
    Choices choices;
}
