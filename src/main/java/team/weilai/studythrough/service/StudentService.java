package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import team.weilai.studythrough.pojo.DTO.ArgDTO;
import team.weilai.studythrough.pojo.LessonStu;
import team.weilai.studythrough.pojo.VO.LessonStuVO;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.websocket.pojo.AuditStu;

/**
 * @author gwj
 * @create 2024/10/11 15:07
 */
public interface StudentService {
    Result<AuditStu> join(String code);

    Result<Page<LessonStuVO>> getList(ArgDTO argDTO);

    Result<Page<LessonStu>> getRecords(Integer pageNum,Integer pageSize);
}
