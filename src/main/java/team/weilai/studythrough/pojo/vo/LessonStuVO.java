package team.weilai.studythrough.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gwj
 * @create 2024/10/11 15:30
 */
@Data
public class LessonStuVO {
    private Long lessonId;
    private String lessonName;
    private String coverUrl;
    private Long userId;
    private String name;
    private Date endTime;
    private Date createTime;
}
