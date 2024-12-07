package team.weilai.studythrough.pojo.main;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gwj
 * @create 2024/10/10 20:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("st_lesson")
public class Lesson {
    @TableId(type = IdType.AUTO)
    private Long lessonId;
    private String lessonName;
    private Long userId;
    private String coverUrl;
    private String inviteCode;
    private Date endTime;
    private Date createTime;
}
