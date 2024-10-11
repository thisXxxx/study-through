package team.weilai.studythrough.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gwj
 * @create 2024/10/11 15:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lesson_stu")
public class LessonStu {
    @TableId(type = IdType.AUTO)
    private Long lessonStuId;
    private Long lessonId;
    private Long userId;
    private Date createTime;
    public LessonStu(Long lessonId,Long userId) {
        this.lessonId = lessonId;
        this.userId = userId;
    }
}
