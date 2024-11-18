package team.weilai.studythrough.pojo.exam.dto;

import lombok.Data;
import team.weilai.studythrough.constants.Constants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author gwj
 * @date 2024/11/12 21:07
 */
@Data
public class ExamQueryDTO {
    private String examName;
    @NotNull(message = Constants.ARG_ERROR)
    private Long lessonId;
    @Min(1)
    private Integer pageNum;
    @Min(1)
    private Integer pageSize;
}
