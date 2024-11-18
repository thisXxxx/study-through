package team.weilai.studythrough.pojo.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gwj
 * @date 2024/11/16 19:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperReviewVO {
    List<QuCommonVO> radioList;
    List<QuCommonVO> multiList;
    List<QuCommonVO> judgeList;
    List<QuCommonVO> bigList;
}
