package team.weilai.studythrough.websocket.pojo;

import lombok.Data;

/**
 * @author gwj
 * @date 2024/11/12 20:46
 */
@Data
public class ExamMsg {
    private String startTime;
    private String endTime;
    private Long keepTime;
    private String examName;
}
