package team.weilai.studythrough.pojo.exam.main;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
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
 * @TableName st_repo
 */
@TableName(value ="st_repo")
@Data
@NoArgsConstructor
public class Repo implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    @ExcelProperty("id")
    private Long repoId;

    /**
     *
     */
    @ExcelProperty("仓库名")
    private String repoTitle;

    /**
     *
     */
    @ExcelProperty("学科")
    private String subject;

    /**
     *
     */
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelIgnore
    private Long createBy;

    @TableField(exist = false)
    @ExcelIgnore
    private static final long serialVersionUID = 1L;

    public Repo(String repoTitle,String subject,Long createBy) {
        this.repoTitle = repoTitle;
        this.subject = subject;
        this.createBy = createBy;
    }
}
