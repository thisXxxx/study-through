package team.weilai.studythrough.pojo.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName question_ans
 */
@TableName(value ="question_ans")
@Data
public class QuestionAns implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long ansId;

    /**
     * 
     */
    private Long questionId;

    /**
     * 0:正确  1：错误
     */
    private Integer isRight;

    /**
     * 
     */
    private String ansContent;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}