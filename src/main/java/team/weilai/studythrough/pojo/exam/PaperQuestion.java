package team.weilai.studythrough.pojo.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName st_paper_question
 */
@TableName(value ="st_paper_question")
@Data
public class PaperQuestion implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long paperQuId;

    /**
     *
     */
    private Long paperId;

    /**
     *
     */
    private Long questionId;

    /**
     *
     */
    private Integer questionType;

    /**
     * 0:未答  1：已答
     */
    private Integer answered;

    /**
     * 主观答案
     */
    private String answer;

    /**
     * 标准答案
     */
    private String standardAns;

    /**
     * 单题分值
     */
    private Integer score;

    /**
     * 实际得分
     */
    private Integer actualScore;

    /**
     * 是否答对
     */
    private Integer isRight;

    @TableField(exist = false)
    private Integer total;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public PaperQuestion(Long paperId,Long questionId) {
        this.paperId = paperId;
        this.questionId = questionId;
    }
}
