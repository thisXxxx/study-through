package team.weilai.studythrough.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.weilai.studythrough.pojo.exam.Repo;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.RepoService;

import javax.annotation.Resource;

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
    @ApiOperation("添加题库")
    public Result<Void> add(String repoTitle,String subject) {
        boolean save = repoService.save(new Repo(repoTitle, subject));
        return save ? Result.ok() : Result.fail();
    }


}
