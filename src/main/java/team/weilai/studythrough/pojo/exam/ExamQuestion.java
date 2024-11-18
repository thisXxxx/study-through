package team.weilai.studythrough.pojo.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 试卷题目关联表
 *
 * @TableName exam_question
 */
@TableName(value ="exam_question")
@Data
@NoArgsConstructor
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public ExamQuestion(Long examId,Long questionId) {
        this.examId = examId;
        this.questionId = questionId;
    }
}
