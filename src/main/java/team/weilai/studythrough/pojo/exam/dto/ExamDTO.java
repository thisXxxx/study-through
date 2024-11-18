package team.weilai.studythrough.pojo.exam.dto;

import lombok.Data;
import team.weilai.studythrough.constants.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author gwj
 * @date 2024/11/12 20:19
 */
@Data
public class ExamDTO {
    /**
     * 课程id
     */
    @NotNull(message = Constants.ARG_ERROR)
    private Long lessonId;

    /**
     * 考试名称
     */
    @NotBlank(message = Constants.ARG_ERROR)
    private String examName;


    private Integer radioScore;

    private Integer multiScore;

    private Integer judgeScore;

    private Integer bigScore;

    private Integer radioCnt;

    private Integer multiCnt;

    private Integer judgeCnt;

    private Integer bigCnt;


    /**
     * 及格分
     */
    @NotNull(message = Constants.ARG_ERROR)
    private Integer passScore;

    @NotNull(message = Constants.ARG_ERROR)
    private Integer totalScore;

    /**
     * 单位：分钟
     */
    private Long keepTime;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    @NotNull(message = Constants.ARG_ERROR)
    private List<Long> questionIds;
}
