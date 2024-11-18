package team.weilai.studythrough.pojo.exam.vo;

import lombok.Data;
import team.weilai.studythrough.pojo.exam.QuestionAns;

import java.util.Date;
import java.util.List;

/**
 * @author gwj
 * @date 2024/11/16 11:02
 */
@Data
public class QuestionDetailVO {
    private Long questionId;
    private String questionContent;
    private String questionSubject;
    private Integer questionType;
    private Integer questionLevel;
    private Date createTime;
    private List<QuestionAns> ansList;
}
