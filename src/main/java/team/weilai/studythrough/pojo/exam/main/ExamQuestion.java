package team.weilai.studythrough.pojo.exam.main;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 试卷题目关联表
 *
 * @TableName st_exam_question
 */
@TableName(value ="st_exam_question")
@Data
@NoArgsConstructor
@ToString
public class ExamQuestion implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long examQuestionId;

    /**
     *
     */
    private Long examId;

    /**
     *
     */
    private Long questionId;

    private Integer questionType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public ExamQuestion(Long examId,Long questionId,Integer questionType) {
        this.examId = examId;
        this.questionId = questionId;
        this.questionType = questionType;
    }
}
