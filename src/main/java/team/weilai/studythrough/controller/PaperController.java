package team.weilai.studythrough.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.weilai.studythrough.pojo.exam.vo.PaperReviewVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.PaperService;
import team.weilai.studythrough.service.QuestionService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gwj
 * @date 2024/11/16 19:19
 */
@RestController
@RequestMapping("/paper")
@Api(tags = "试卷相关")
public class PaperController {

    @Resource
    private PaperService paperService;
    @Resource
    private QuestionService questionService;


    @GetMapping("/preview")
    @ApiOperation("预览试卷")
    @PreAuthorize("hasAuthority('1')")
    public Result<PaperReviewVO> preview(@RequestParam("ids") List<Long> ids) {
        return questionService.preview(ids);
    }

   /* @GetMapping("/detail")
    @ApiOperation("试卷详情")
    public Result<>*/
}
