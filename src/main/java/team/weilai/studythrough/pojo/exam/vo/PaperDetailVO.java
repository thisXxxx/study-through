package team.weilai.studythrough.pojo.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.weilai.studythrough.pojo.exam.main.PaperQuestion;

import java.util.List;

/**
 * @author gwj
 * @date 2024/11/18 21:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperDetailVO {
    private List<PaperQuestion> radioList;
    private List<PaperQuestion> multiList;
    private List<PaperQuestion> judgeList;
    private List<PaperQuestion> bigList;
    private String examName;
    private Long keepTime;
}
