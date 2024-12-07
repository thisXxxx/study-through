package team.weilai.studythrough.pojo.exam.dto;

import lombok.Data;
import team.weilai.studythrough.constants.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author gwj
 * @date 2024/11/12 8:51
 */
@Data
public class QuestionDTO {
    @NotNull(message = Constants.ARG_ERROR)
    private Integer questionType;

    @NotNull(message = Constants.ARG_ERROR)
    private Integer questionLevel;

    @NotBlank(message = Constants.ARG_ERROR)
    private String questionContent;

    @NotBlank(message = Constants.ARG_ERROR)
    private String questionSubject;

    private Long repoId;
    private String questionAnalysis;
}
