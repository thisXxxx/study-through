package team.weilai.studythrough.pojo.exam.main;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 考试记录
 * @TableName st_paper
 */
@TableName(value ="st_paper")
@Data
@Document(indexName = "paper")
public class Paper implements Serializable {
    /**
     * 试卷ID
     */
    @TableId(type = IdType.AUTO)
    @Id
    private Long paperId;

    /**
     * 用户ID
     */
    @Field(type = FieldType.Long)
    private Long userId;

    /**
     * 考试ID
     */
    @Field(type = FieldType.Long)
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
    @Field(type = FieldType.Long)
    private Long userTime;

    /**
     * 试卷总分
     */
    @Field(type = FieldType.Integer)
    private Integer totalScore;

    /**
     * 及格分
     */
    @Field(type = FieldType.Integer)
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
    @Field(type = FieldType.Integer)
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
