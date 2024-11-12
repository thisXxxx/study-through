package team.weilai.studythrough.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.weilai.studythrough.pojo.exam.dto.QuestionDTO;
import team.weilai.studythrough.pojo.exam.dto.QuestionQueryDTO;
import team.weilai.studythrough.pojo.exam.vo.QuestionVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.QuestionService;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    public Result<Void> add(@Valid QuestionDTO questionDTO) {
        return questionService.add(questionDTO);
    }

    @GetMapping("/query")
    @ApiOperation("查询试题")
    public Result<Page<QuestionVO>> query(@Valid QuestionQueryDTO queryDTO) {
        return questionService.select(queryDTO);
    }
}
