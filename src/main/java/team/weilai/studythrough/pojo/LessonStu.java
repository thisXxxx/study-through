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
    private String lessonName;
    private String stuClass;
    private String name;
    private Long userId;
    private Long teaId;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Integer isRead;
    public LessonStu(Long lessonId,Long userId,Long teaId,String lessonName,String stuClass,String name) {
        this.teaId = teaId;
        this.lessonId = lessonId;
        this.userId = userId;
        this.lessonName = lessonName;
        this.stuClass = stuClass;
        this.name = name;
    }
}
