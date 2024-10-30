package team.weilai.studythrough.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/10/26 9:43
 */
@Data
public class LessonGetVO {
    private String lessonName;
    private String name;
    private String coverUrl;
    private Date createTime;
    private Date endTime;
}
