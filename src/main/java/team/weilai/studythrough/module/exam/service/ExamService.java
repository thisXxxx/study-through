package team.weilai.studythrough.module.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import team.weilai.studythrough.pojo.exam.main.Exam;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.exam.dto.ExamDTO;
import team.weilai.studythrough.pojo.exam.dto.ExamQueryDTO;
import team.weilai.studythrough.pojo.exam.dto.NoExam;
import team.weilai.studythrough.pojo.vo.Result;

import java.util.List;

/**
* @author 86159
* @description 针对表【exam】的数据库操作Service
* @createDate 2024-11-12 19:54:00
*/
public interface ExamService extends IService<Exam> {

    Result<Void> add(ExamDTO dto);

    Result<Page<Exam>> getExam(ExamQueryDTO queryDTO);

    Result<List<NoExam>> getPart(Long examId);
}
