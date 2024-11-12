package team.weilai.studythrough.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/11/7 20:35
 */
@Data
@TableName("sign_stu")
@NoArgsConstructor
@AllArgsConstructor
public class SignStu {
    @TableId(type = IdType.AUTO)
    private Long signStuId;
    private Long signId;
    private Long stuId;
    private Integer status;
    private Date createTime;
    public SignStu(Long signId,Long stuId,Integer status) {
        this.signId = signId;
        this.stuId = stuId;
        this.status = status;
    }
}
