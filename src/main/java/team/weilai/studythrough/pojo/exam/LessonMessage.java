package team.weilai.studythrough.pojo.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @TableName lesson_message
 */
@TableName(value ="lesson_message")
@Data
@NoArgsConstructor
public class LessonMessage implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long lessonMessageId;

    /**
     * 1：签到   2：考试
     */
    private Integer type;

    /**
     *
     */
    private String content;

    /**
     *
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public LessonMessage(Integer type,String content) {
        this.type = type;
        this.content = content;
    }
}
