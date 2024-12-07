package team.weilai.studythrough.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.weilai.studythrough.util.enums.StatusCodeEnum;
import team.weilai.studythrough.pojo.dto.ArgDTO;
import team.weilai.studythrough.pojo.main.LessonStu;
import team.weilai.studythrough.pojo.exam.main.LessonMessage;
import team.weilai.studythrough.pojo.vo.LessonStuVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.LessonMessageService;
import team.weilai.studythrough.service.StudentService;
import team.weilai.studythrough.websocket.pojo.AuditStu;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author gwj
 * @create 2024/10/11 15:06
 */
@RestController
@RequestMapping("/stu")
@Api(tags = "学生")
@PreAuthorize("hasAuthority('0')")
public class StudentController {

    @Resource
    private StudentService studentService;
    @Resource
    private LessonMessageService lessonMessageService;

    @PostMapping("/join")
    @ApiOperation("学生加入课程")
    public Result<AuditStu> join(@NotNull String code) {
        return studentService.join(code);
    }

    @GetMapping("/getList")
    @ApiOperation("学生加入的课程列表")
    public Result<Page<LessonStuVO>> getList(@Valid ArgDTO argDTO) {
        return studentService.getList(argDTO);
    }

    @GetMapping("/getRecords")
    @ApiOperation("获取课程申请消息记录")
    public Result<Page<LessonStu>> getRecords(@NotNull Integer pageNum,@NotNull Integer pageSize) {
        if (pageNum <= 0 || pageSize <= 0) {
            return Result.fail(StatusCodeEnum.VALID_ERROR);
        }
        return studentService.getRecords(pageNum,pageSize);
    }

    @GetMapping("/getMessage")
    @ApiOperation("学生查看课程消息")
    public Result<Page<LessonMessage>> getMessage(@NotNull Integer pageNum,
                                                  @NotNull Integer pageSize,
                                                  @NotNull Long lessonId) {
        if (pageNum <= 0 || pageSize <= 0) {
            return Result.fail(StatusCodeEnum.VALID_ERROR);
        }
        Page<LessonMessage> page = new Page<>(pageNum,pageSize);
        lessonMessageService.query().eq("lesson_id",lessonId).page(page);
        return Result.ok(page);
    }
}
