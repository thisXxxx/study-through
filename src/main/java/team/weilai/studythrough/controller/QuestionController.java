package team.weilai.studythrough.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.weilai.studythrough.pojo.exam.QuestionAns;
import team.weilai.studythrough.pojo.exam.dto.QuestionDTO;
import team.weilai.studythrough.pojo.exam.dto.QuestionQueryDTO;
import team.weilai.studythrough.pojo.exam.vo.QuestionDetailVO;
import team.weilai.studythrough.pojo.exam.vo.QuestionVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.QuestionService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author gwj
 * @date 2024/11/12 8:21
 */
@RestController
@RequestMapping("/qu")
@Api(tags = "题目")
public class QuestionController {

    @Resource
    private QuestionService questionService;


    @PostMapping("/add")
    @ApiOperation("添加试题")
    @PreAuthorize("hasAuthority('1')")
    public Result<Void> add(@Valid QuestionDTO questionDTO, @RequestBody List<QuestionAns> ansList) {
        return questionService.add(questionDTO,ansList);
    }

    @GetMapping("/query")
    @ApiOperation("查询试题")
    @PreAuthorize("hasAuthority('1')")
    public Result<Page<QuestionVO>> query(@Valid QuestionQueryDTO queryDTO) {
        return questionService.select(queryDTO);
    }

    @GetMapping("/getDetail")
    @ApiOperation("查询题目详情")
    public Result<QuestionDetailVO> getDetail(@NotNull Long questionId) {
        return questionService.getDetail(questionId);
    }
}
