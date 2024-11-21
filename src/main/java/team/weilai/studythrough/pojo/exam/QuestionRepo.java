package team.weilai.studythrough.pojo.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @TableName st_question_repo
 */
@TableName(value ="st_question_repo")
@Data
@NoArgsConstructor
public class QuestionRepo implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long questionRepoId;

    /**
     *
     */
    private Long questionId;

    /**
     *
     */
    private Long repoId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public QuestionRepo(Long questionId,Long repoId) {
        this.questionId = questionId;
        this.repoId = repoId;
    }
}
