package team.weilai.studythrough.pojo.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import static team.weilai.studythrough.constants.Constants.ARG_ERROR;

/**
 * @author gwj
 * @since 2024/5/16 22:34
 */
@Data
public class LoginDTO {

    @NotBlank(message = ARG_ERROR)
    private String username;

    @NotBlank(message = ARG_ERROR)
    private String password;
}
