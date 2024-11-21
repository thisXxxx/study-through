package team.weilai.studythrough.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/11/7 15:28
 */
@Data
@TableName("st_sign")
public class Sign {
    @TableId(type = IdType.AUTO)
    private Long signId;
    private Long lessonId;
    private String longitude;
    private Integer distance;
    private String latitude;
    private Integer keepTime;
    private Date createTime;
}
