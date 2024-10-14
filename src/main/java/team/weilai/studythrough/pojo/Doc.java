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
 * @create 2024/10/11 17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("doc")
public class Doc {
    @TableId(type = IdType.AUTO)
    private Long docId;
    private Long lessonId;
    private Long parentId;
    private Integer isDir;
    private String docTitle;
    private String docRef;
    private Date createTime;
    public Doc(Long lessonId) {
        this.lessonId = lessonId;
    }
    public Doc(Long lessonId,String docTitle) {
        this.lessonId = lessonId;
        this.docTitle = docTitle;
    }
    public Doc(String docTitle,Long parentId) {
        this.parentId = parentId;
        this.docTitle = docTitle;
    }

    public Doc(String docTitle,String docRef,Long parentId) {
        this.docTitle = docTitle;
        this.docRef = docRef;
        this.parentId = parentId;
    }
    public Doc(Long lessonId,String docTitle,String docRef) {
        this.lessonId = lessonId;
        this.docTitle = docTitle;
        this.docRef = docRef;
    }
}
