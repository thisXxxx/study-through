package team.weilai.studythrough.pojo.exam.dto;

import lombok.Data;
import team.weilai.studythrough.constants.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author gwj
 * @date 2024/11/19 17:15
 */
@Data
public class PaperAnswerDTO {
    @NotBlank(message = Constants.ARG_ERROR)
    private String answer;
    @NotNull(message = Constants.ARG_ERROR)
    private Long paperId;
    @NotNull(message = Constants.ARG_ERROR)
    private Long paperQuId;
}
