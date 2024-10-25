package team.weilai.studythrough.pojo.vo;

import lombok.Data;

/**
 * @author gwj
 * @date 2024/10/25 16:29
 */
@Data
public class UserVO {
    private Long userId;
    private String stuClass;
    private String username;
    private String name;
    private String sex;
    private String email;
    private Integer status;
}
