package team.weilai.studythrough.pojo.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/11/18 19:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisExam {
    private Long lessonId;
    private Date startTime;
    private Date endTime;
    private String examName;
    private Long keepTime;
    private Integer totalScore;
    private Integer passScore;
    private Integer has_saq;
}
