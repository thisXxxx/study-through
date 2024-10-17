package team.weilai.studythrough.websocket.pojo;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gwj
 * @create 2024/10/15 21:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditStu {
    private Long lessonStuId;
    private Long toUser;
    private String stuClass;
    private String name;
    private String lessonName;
    private Integer status;
    private Date time;
    private String coverUrl;
    private Long noRead;
    public AuditStu(Long toUser,String lessonName, Integer status,Date time) {
        this.time = time;
        this.toUser = toUser;
        this.lessonName = lessonName;
        this.status = status;
    }
}
