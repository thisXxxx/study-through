package team.weilai.studythrough.pojo.dto;

import lombok.Data;
import team.weilai.studythrough.constants.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author gwj
 * @date 2024/11/7 15:34
 */
@Data
public class SignDTO {
    @NotNull(message = Constants.ARG_ERROR)
    private Long lessonId;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    private Integer distance;
    @NotNull(message = Constants.ARG_ERROR)
    private Integer keepTime;
}
