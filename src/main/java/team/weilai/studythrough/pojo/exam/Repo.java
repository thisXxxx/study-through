package team.weilai.studythrough.pojo.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @TableName repo
 */
@TableName(value ="repo")
@Data
@NoArgsConstructor
public class Repo implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long repoId;

    /**
     *
     */
    private String repoTitle;

    /**
     *
     */
    private String subject;

    /**
     *
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Repo(String repoTitle,String subject) {
        this.repoTitle = repoTitle;
        this.subject = subject;
    }
}
