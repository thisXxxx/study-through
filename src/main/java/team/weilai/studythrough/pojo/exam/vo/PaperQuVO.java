package team.weilai.studythrough.pojo.exam.vo;

import lombok.Data;
import team.weilai.studythrough.pojo.exam.main.QuestionAns;

import java.util.List;

/**
 * @author gwj
 * @date 2024/11/19 17:32
 */
@Data
public class PaperQuVO {
    private String questionContent;
    private List<QuestionAns> ansList;
    private String answer;
    private Integer questionType;
}
