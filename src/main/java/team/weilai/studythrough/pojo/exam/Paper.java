package team.weilai.studythrough.pojo.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 考试记录
 * @TableName st_paper
 */
@TableName(value ="st_paper")
@Data
public class Paper implements Serializable {
    /**
     * 试卷ID
     */
    @TableId(type = IdType.AUTO)
    private Long paperId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 考试ID
     */
    private Long examId;

    /**
     * 考试标题
     */
    private String examName;

    /**
     * 考试时长
     */
    private Long keepTime;

    /**
     * 用户时长
     */
    private Long userTime;

    /**
     * 试卷总分
     */
    private Integer totalScore;

    /**
     * 及格分
     */
    private Integer passScore;

    /**
     * 客观分
     */
    private Integer objScore;

    /**
     * 主观分
     */
    private Integer subjScore;

    /**
     * 用户得分
     */
    private Integer userScore;

    /**
     * 是否包含简答题
     */
    private Integer hasSaq;

    /**
     * 试卷状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 截止时间
     */
    private Date endTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
