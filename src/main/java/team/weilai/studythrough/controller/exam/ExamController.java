package team.weilai.studythrough.controller.exam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.pojo.exam.Exam;
import team.weilai.studythrough.pojo.exam.Paper;
import team.weilai.studythrough.pojo.exam.dto.ExamDTO;
import team.weilai.studythrough.pojo.exam.dto.ExamQueryDTO;
import team.weilai.studythrough.pojo.exam.vo.QuCommonVO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.ExamService;
import team.weilai.studythrough.service.PaperService;
import team.weilai.studythrough.util.CommonUtils;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author gwj
 * @date 2024/11/12 19:59
 */
@RestController
@RequestMapping("/exam")
@Api(tags = "考试相关")
public class ExamController {

    @Resource
    private ExamService examService;
    @Resource
    private PaperService paperService;


    @PostMapping("/add")
    @ApiOperation("添加考试")
    public Result<Void> add(@Valid @RequestBody ExamDTO dto) {
        if (dto.getQuestionIds().isEmpty()) {
            return Result.fail(StatusCodeEnum.VALID_ERROR);
        }
        return examService.add(dto);
    }

    @GetMapping("/query")
    @ApiOperation("查看该课程所有考试")
    public Result<Page<Exam>> query(@Valid ExamQueryDTO queryDTO) {
        Page<Exam> page = new Page<>(queryDTO.getPageNum(),queryDTO.getPageSize());
        String examName = queryDTO.getExamName();
        boolean b = examName != null && !examName.isEmpty();
        examService.query().like(b,"exam_name",examName)
                .eq("lesson_id",queryDTO.getLessonId()).page(page);
        return Result.ok(page);
    }


    @PostMapping("/enter")
    @ApiOperation("进入考试")
    public Result<Long> enter(@NotNull Long examId,@NotNull Long lessonId) {
        Paper one = paperService.query().eq("exam_id", examId).eq("user_id", CommonUtils.getUserId()).one();
        if (one != null) {
            return Result.ok(one.getPaperId(),"数据已存在");
        }
        return paperService.enter(examId,lessonId);
    }

}
