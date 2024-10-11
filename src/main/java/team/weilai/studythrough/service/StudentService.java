package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import team.weilai.studythrough.pojo.DTO.ArgDTO;
import team.weilai.studythrough.pojo.VO.LessonStuVO;
import team.weilai.studythrough.pojo.VO.Result;

/**
 * @author gwj
 * @create 2024/10/11 15:07
 */
public interface StudentService {
    Result<Void> join(String code);

    Result<Page<LessonStuVO>> getList(ArgDTO argDTO);
}
