package team.weilai.studythrough.websocket.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gwj
 * @date 2024/11/7 19:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignMsg {
    private Long signId;
    /**
     * 持续时间
     */
    private Integer time;
}
