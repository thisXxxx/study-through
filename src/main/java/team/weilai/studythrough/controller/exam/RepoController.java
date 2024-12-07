package team.weilai.studythrough.controller.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.weilai.studythrough.util.enums.StatusCodeEnum;
import team.weilai.studythrough.pojo.exam.main.Repo;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.module.exam.service.RepoService;
import team.weilai.studythrough.util.CommonUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gwj
 * @date 2024/11/12 9:25
 */
@RestController
@RequestMapping("/repo")
@Api(tags = "题库管理")
public class RepoController {

    @Resource
    private RepoService repoService;


    @PostMapping("/add")
    @ApiOperation("创建题库")
    @PreAuthorize("hasAuthority('1')")
    public Result<Void> add(String repoTitle,String subject) {
        Long userId = CommonUtils.getUserId();
        boolean save = repoService.save(new Repo(repoTitle, subject,userId));
        return save ? Result.ok() : Result.fail();
    }

    @PostMapping("/addQuestion")
    @ApiOperation("向题库中添加题目")
    @PreAuthorize("hasAuthority('1')")
    public Result<Void> addQuestion(@RequestParam List<Long> ids, Long repoId) {
        if (ids == null || ids.isEmpty()) {
            return Result.fail(StatusCodeEnum.VALID_ERROR);
        }
        return repoService.add(ids,repoId);
    }

    @GetMapping("/repoSelf")
    @ApiOperation("查看自己创建的题库")
    @PreAuthorize("hasAuthority('1')")
    public Result<Page<Repo>> repoSelf(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        if (pageNum <= 0 || pageSize <= 0) {
            return Result.fail(StatusCodeEnum.VALID_ERROR);
        }
        Page<Repo> page = new Page<>(pageNum,pageSize);
        repoService.page(page,new QueryWrapper<Repo>()
                .eq("create_by",CommonUtils.getUserId())
                .select("repo_id","repo_title","subject","create_time"));
        return Result.ok(page);
    }




}
