package team.weilai.studythrough.pojo.exam.vo;

import lombok.Data;
import team.weilai.studythrough.pojo.exam.main.QuestionAns;

import java.util.List;

/**
 * @author gwj
 * @date 2024/11/16 19:28
 */
@Data
public class QuCommonVO {
    private String questionContent;
    private List<QuestionAns> ansList;
    private Integer questionType;
}
