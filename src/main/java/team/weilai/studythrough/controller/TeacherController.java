package team.weilai.studythrough.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.weilai.studythrough.pojo.DTO.ArgDTO;
import team.weilai.studythrough.pojo.DTO.LessonDTO;
import team.weilai.studythrough.pojo.Lesson;
import team.weilai.studythrough.pojo.VO.LessonVO;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.LessonService;
import team.weilai.studythrough.util.CommonUtils;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * @author gwj
 * @create 2024/10/9 21:26
 */
@RestController
@RequestMapping("/tea")
@Api(tags = "老师")
@PreAuthorize("hasAuthority('1')")
public class TeacherController {

    @Resource
    private LessonService lessonService;


    @PostMapping("/create")
    @ApiOperation("创建课程")
    public Result<Void> createLesson(LessonDTO lessonDTO) {
        return lessonService.createLesson(lessonDTO);
    }

    @GetMapping("/getInvite")
    @ApiOperation("获取课程邀请码")
    public Result<String> getInvite(@NotNull Long lessonId) {
        Long userId = CommonUtils.getUserId();
        Lesson one = lessonService.query()
                .select("invite_code")
                .eq("lesson_id", lessonId)
                .eq("user_id",userId).one();
        if (one == null) {
            return Result.fail();
        }
        return Result.ok(one.getInviteCode());
    }

    @GetMapping("/getList")
    @ApiOperation("获取创建过的课程列表")
    public Result<Page<LessonVO>> getList(@Valid ArgDTO argDTO) {
        return lessonService.getList(argDTO);
    }




}
