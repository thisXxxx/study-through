package team.weilai.studythrough.controller.exam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.pojo.exam.Paper;
import team.weilai.studythrough.pojo.exam.dto.PaperAnswerDTO;
import team.weilai.studythrough.pojo.exam.vo.PaperDetailVO;
import team.weilai.studythrough.pojo.exam.vo.PaperQuVO;
import team.weilai.studythrough.pojo.exam.vo.PaperReviewVO;
import team.weilai.studythrough.pojo.exam.vo.QuCommonVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.PaperQuestionService;
import team.weilai.studythrough.service.PaperService;
import team.weilai.studythrough.service.QuestionService;
import team.weilai.studythrough.util.CommonUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    @Resource
    private PaperQuestionService paperQuestionService;


    @GetMapping("/preview")
    @ApiOperation("预览试卷")
    @PreAuthorize("hasAuthority('1')")
    public Result<PaperReviewVO> preview(@RequestParam("ids") List<Long> ids) {
        return questionService.preview(ids);
    }

    @GetMapping("/detail")
    @ApiOperation("试卷详情")
    @PreAuthorize("hasAuthority('0')")
    public Result<PaperDetailVO> detail(@NotNull Long paperId) {
        return paperQuestionService.detail(paperId);
    }


    @GetMapping("/getQu")
    @ApiOperation("考试中查看题目")
    @PreAuthorize("hasAuthority('0')")
    public Result<PaperQuVO> getQu(@NotNull Long questionId,@NotNull Long paperId) {
        return questionService.getQu(questionId,paperId);
    }

    @PostMapping("/fillAns")
    @ApiOperation("填充答案")
    @PreAuthorize("hasAuthority('0')")
    public Result<Void> fillAns(@Valid @RequestBody PaperAnswerDTO answerDTO) {
        Paper one = paperService.query().select("user_id").eq("paper_id", answerDTO.getPaperId()).one();
        if (one == null || !one.getUserId().equals(CommonUtils.getUserId())) {
            return Result.fail(StatusCodeEnum.AUTHORIZED);
        }
        return paperQuestionService.fillAns(answerDTO);
    }

}
