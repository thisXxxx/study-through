package team.weilai.studythrough.pojo.VO;

import lombok.Data;

import java.util.Date;

/**
 * @author gwj
 * @create 2024/10/11 10:31
 */
@Data
public class LessonVO {
    private Long lessonId;
    private String lessonName;
    private String coverUrl;
    private Date endTime;
    private Date createTime;
}
