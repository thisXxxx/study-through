package team.weilai.studythrough.pojo.exam.main;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName st_exam
 */
@TableName(value ="st_exam")
@Data
public class Exam implements Serializable {
    /**
     * 考试id
     */
    @TableId(type = IdType.AUTO)
    private Long examId;

    /**
     * 课程id
     */
    private Long lessonId;

    /**
     * 考试名称
     */
    private String examName;

    /**
     * 总分
     */
    private Integer totalScore;

    /**
     * 及格分
     */
    private Integer passScore;

    /**
     * 单位：分钟
     */
    private Long keepTime;

    /**
     * 开始时间
     */
    private Date startTime;

    private Integer radioScore;
    private Integer multiScore;
    private Integer judgeScore;
    private Integer bigScore;
    private Integer radioCnt;
    private Integer multiCnt;
    private Integer judgeCnt;
    private Integer bigCnt;

    /**
     * 结束时间
     */
    private Date endTime;

    private Date createTime;

    /**
     * 0：未交
     * 2：待批阅
     * 3：已完成
     */
    @TableField(exist = false)
    private Integer hasFin;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
