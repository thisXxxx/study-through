package team.weilai.studythrough.pojo.dto;

import lombok.Data;
import team.weilai.studythrough.constants.Constants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author gwj
 * @create 2024/10/12 22:02
 */
@Data
public class FileDTO {
    @Min(1)
    private Integer pageNum;
    @Min(1)
    private Integer pageSize;
    private String docTitle;
    @NotNull(message = Constants.ARG_ERROR)
    private Long parentId;
    @NotNull(message = Constants.ARG_ERROR)
    private Long lessonId;
}
