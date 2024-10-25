package team.weilai.studythrough.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author gwj
 * @date 2024/10/25 19:07
 */
@Data
public class UserDTO {
    @Min(1)
    private Integer pageNum;
    @Min(1)
    private Integer pageSize;
    private String username;
    private String name;
    private Integer status;
}
