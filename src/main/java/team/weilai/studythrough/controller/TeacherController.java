package team.weilai.studythrough.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.pojo.DTO.ArgDTO;
import team.weilai.studythrough.pojo.DTO.FileDTO;
import team.weilai.studythrough.pojo.DTO.LessonDTO;
import team.weilai.studythrough.pojo.Doc;
import team.weilai.studythrough.pojo.Lesson;
import team.weilai.studythrough.pojo.VO.LessonVO;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.DocService;
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
    @Resource
    private DocService docService;


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

    @PostMapping("/makeDir")
    @ApiOperation("创建文件夹")
    @ApiImplicitParam(name = "isRoot", value = "0:不是根目录 1：是根目录")
    public Result<Void> makeDir(Long parentId,Long lessonId,String dirName) {
        return docService.makeDir(parentId,lessonId,dirName);
    }

    @PostMapping("/makeFile")
    @ApiOperation("上传文件")
    public Result<Void> makeFile(@RequestPart("file")MultipartFile file,Long parentId,Long lessonId) {
        return docService.makeFile(file,parentId,lessonId);
    }

    @GetMapping("/listFile")
    @ApiOperation("根据父id获取文件列表")
    @PreAuthorize("hasAnyAuthority('0','1')")
    public Result<Page<Doc>> listFile(@Valid FileDTO fileDTO) {
        return docService.listFile(fileDTO);
    }

}
