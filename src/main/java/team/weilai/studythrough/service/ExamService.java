package team.weilai.studythrough.service;

import team.weilai.studythrough.pojo.exam.Exam;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.exam.dto.ExamDTO;
import team.weilai.studythrough.pojo.vo.Result;

/**
* @author 86159
* @description 针对表【exam】的数据库操作Service
* @createDate 2024-11-12 19:54:00
*/
public interface ExamService extends IService<Exam> {

    Result<Void> add(ExamDTO dto);
}
