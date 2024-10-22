package team.weilai.studythrough.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.mapper.LessonStuMapper;
import team.weilai.studythrough.pojo.DTO.LoginDTO;
import team.weilai.studythrough.pojo.LessonStu;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.UserService;
import team.weilai.studythrough.util.CommonUtils;
import team.weilai.studythrough.util.MinioUtil;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author gwj
 * @create 2024/10/9 17:58
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private MinioUtil minioUtil;
    @Resource
    private LessonStuMapper lessonStuMapper;


    @PostMapping("/login")
    @ApiOperation("登录")
    @PreAuthorize("permitAll()")
    public Result<Map<String, String>> login(@Valid @RequestBody LoginDTO loginDTO) {

        return userService.login(loginDTO);
    }

    @PostMapping("/upFile")
    @ApiOperation("普通上传文件")
    @PreAuthorize("hasAnyAuthority('0','1','2')")
    public Result<String> upFile(@RequestPart("file") MultipartFile file) {
        String upload = minioUtil.upload(file);
        return Result.ok(upload);
    }

    @GetMapping("/getNoRead")
    @ApiOperation("查询申请课程相关未读消息个数")
    @PreAuthorize("hasAnyAuthority('0','1')")
    public Result<Long> getNoRead() {
        Long userId = CommonUtils.getUserId();
        Long l;
        Integer role = CommonUtils.getRole();
        if (role == 1) {
            l = lessonStuMapper.selectCount(new QueryWrapper<LessonStu>()
                    .eq("is_read", 0).eq("tea_id", userId));
        } else {
            l = lessonStuMapper.selectCount(new QueryWrapper<LessonStu>()
                    .eq("is_read", 0).eq("user_id", userId));
        }
        return Result.ok(l);
    }

    @PutMapping("/setRead")
    @ApiOperation("将课程申请消息置为已读")
    @PreAuthorize("hasAnyAuthority('0','1')")
    public Result<Void> setRead() {
        Integer role = CommonUtils.getRole();
        Long userId = CommonUtils.getUserId();
        if (role == 1) {
            lessonStuMapper.update(new UpdateWrapper<LessonStu>().set("is_read", 1).eq("tea_id",userId));
        }else {
            lessonStuMapper.update(new UpdateWrapper<LessonStu>().set("is_read",1).eq("user_id",userId));
        }
        return Result.ok();
    }
}
