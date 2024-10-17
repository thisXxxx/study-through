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
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.pojo.DTO.ArgDTO;
import team.weilai.studythrough.pojo.DTO.FileDTO;
import team.weilai.studythrough.pojo.DTO.LessonDTO;
import team.weilai.studythrough.pojo.Doc;
import team.weilai.studythrough.pojo.Lesson;
import team.weilai.studythrough.pojo.LessonStu;
import team.weilai.studythrough.pojo.VO.LessonVO;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.DocService;
import team.weilai.studythrough.service.LessonService;
import team.weilai.studythrough.service.TeacherService;
import team.weilai.studythrough.util.CommonUtils;
import team.weilai.studythrough.websocket.pojo.AuditStu;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


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
    @Resource
    private TeacherService teacherService;


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
    @ApiOperation("上传课程文件")
    public Result<Void> makeFile(String filePath,String fileName,Long parentId,Long lessonId) {
        return docService.makeFile(filePath,fileName,parentId,lessonId);
    }

    @GetMapping("/listFile")
    @ApiOperation("根据父id获取文件列表")
    @PreAuthorize("hasAnyAuthority('0','1')")
    public Result<Page<Doc>> listFile(@Valid FileDTO fileDTO) {
        return docService.listFile(fileDTO);
    }

    @GetMapping("/auditList")
    @ApiOperation("获取课程申请列表")
    public Result<Page<LessonStu>> auditList(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        if (pageNum <= 0 || pageSize <= 0) {
            return Result.fail(StatusCodeEnum.VALID_ERROR);
        }
        return teacherService.auditList(pageNum,pageSize);
    }

    @PutMapping("/audit")
    @ApiOperation("审核申请课程学生")
    @ApiImplicitParam(name = "choose",value = "1:通过 2：拒绝")
    public Result<AuditStu> audit(@NotNull Long lessonStuId, @NotNull Integer choose) {
        if (choose != 1 && choose != 2) {
            return Result.fail(StatusCodeEnum.VALID_ERROR);
        }
        return teacherService.audit(lessonStuId,choose);
    }

    @PostMapping("/bigUpload")
    @ApiOperation("分片上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "md5",value = "编码"),
            @ApiImplicitParam(name = "chunk",value = "当前切片数"),
            @ApiImplicitParam(name = "total",value = "切片总数")
    })
    public Result<String> uploadBig(@RequestPart("file")MultipartFile file, Integer chunk,Integer total,String md5) {
        return teacherService.uploadBig(file,chunk,total,md5);
    }

    @PostMapping("/merge")
    @ApiOperation("合并分片文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "md5",value = "编码"),
            @ApiImplicitParam(name = "fileName",value = "文件名称"),
            @ApiImplicitParam(name = "total",value = "分片总数")
    })
    public Result<String> mergePart(@NotNull String md5,@NotNull String fileName,@NotNull Integer total) {
        return teacherService.mergePart(md5,fileName,total);
    }

    @GetMapping("/getNoUp")
    @ApiOperation("查询未上传索引")
    public Result<List<Integer>> getNoUp(@NotNull String md5) {
        return teacherService.getNoUp(md5);
    }

}
