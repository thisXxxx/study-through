package team.weilai.studythrough.pojo.exam.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/11/12 9:40
 */
@Data
public class QuestionVO {
    private Long questionId;
    private String questionContent;
    private String questionSubject;
    private Integer questionType;
    private Integer questionLevel;
    private Date createTime;
}
