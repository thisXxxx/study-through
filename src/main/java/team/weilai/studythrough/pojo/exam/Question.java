package team.weilai.studythrough.pojo.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName st_question
 */
@TableName(value ="st_question")
@Data
public class Question implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long questionId;

    /**
     * 0:单选  1:多选 2:判断  3:解答
     */
    private Integer questionType;

    /**
     * 0:简单 1：中等  2:困难
     */
    private Integer questionLevel;

    /**
     *
     */
    private String questionContent;

    /**
     *
     */
    private String questionSubject;

    /**
     *
     */
    private Date createTime;

    /**
     * 题目解析
     */
    private String questionAnalysis;

    /**
     *
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
