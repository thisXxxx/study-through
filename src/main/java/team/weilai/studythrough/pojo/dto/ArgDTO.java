package team.weilai.studythrough.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author gwj
 * @create 2024/10/11 11:14
 */
@Data
public class ArgDTO {
    @Min(1)
    private Integer pageNum;
    @Min(1)
    private Integer pageSize;
    private String name;
}
