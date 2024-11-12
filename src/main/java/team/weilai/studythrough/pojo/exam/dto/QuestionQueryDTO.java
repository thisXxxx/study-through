package team.weilai.studythrough.pojo.exam.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author gwj
 * @date 2024/11/12 16:21
 */
@Data
public class QuestionQueryDTO {
    /**
     * 0:单选  1:多选 2:判断  3:解答
     */
    private Integer type;

    /**
     * 0:简单 1：中等  2:困难
     */
    private Integer level;

    /**
     *
     */
    private String subject;

    @Min(1)
    private Integer pageNum;
    @Min(1)
    private Integer pageSize;
}
