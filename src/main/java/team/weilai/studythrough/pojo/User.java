package team.weilai.studythrough.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gwj
 * @create 2024/10/9 17:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long userId;
    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("用户名")
    private String username;

    @ExcelIgnore
    private String password;

    @ExcelIgnore
    private String email;

    @ExcelProperty("班级")
    private String stuClass;
    @ExcelProperty("性别")
    private String sex;

    @ExcelIgnore
    private Integer status;

    @ExcelIgnore
    private Date createTime;

    @ExcelIgnore
    private Date updateTime;
}
