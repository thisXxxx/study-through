package team.weilai.studythrough.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.weilai.studythrough.pojo.DTO.ArgDTO;
import team.weilai.studythrough.pojo.VO.LessonStuVO;
import team.weilai.studythrough.pojo.VO.LessonVO;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.StudentService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

    @PostMapping("/join")
    @ApiOperation("学生加入课程")
    public Result<Void> join(@NotNull String code) {
        return studentService.join(code);
    }

    @GetMapping("/getList")
    @ApiOperation("学生加入的课程列表")
    public Result<Page<LessonStuVO>> getList(@Valid ArgDTO argDTO) {
        return studentService.getList(argDTO);
    }

}
