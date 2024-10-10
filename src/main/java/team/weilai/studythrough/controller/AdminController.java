package team.weilai.studythrough.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.AdminService;
import team.weilai.studythrough.util.FileTypeUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gwj
 * @create 2024/10/9 18:04
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "管理员")
@PreAuthorize("hasAuthority('2')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Resource
    private FileTypeUtil fileTypeUtil;

    @PostMapping("/uploadUser")
    @ApiOperation("导入用户文件")
    @ApiImplicitParam(name = "type",value = "0：学生 1：老师")
    public Result<Void> uploadStu(@RequestPart("file") MultipartFile file,@NotNull Integer type) {
        if (type != 0 && type != 1) {
            return Result.fail(StatusCodeEnum.VALID_ERROR);
        }
        if (file.isEmpty()) {
            return Result.fail("文件不得为空");
        }
        if (!fileTypeUtil.isExcel(file)) {
            return Result.fail("文件格式错误");
        }
        return adminService.uploadStu(file,type);
    }

    @ApiOperation("获得导入用户样表")
    @GetMapping("/getStuSample")
    public void getStuSample(HttpServletResponse response) throws IOException {
        adminService.getStuSample(response);
    }


}
