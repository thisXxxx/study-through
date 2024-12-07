package team.weilai.studythrough.pojo.main;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/10/26 9:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("st_unread")
public class Unread {
    @TableId(type = IdType.AUTO)
    private Long unreadId;
    private Long messageId;
    private Long userId;
    private Long lessonId;
    private Date createTime;
}
