package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.pojo.main.LessonStu;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.websocket.pojo.AuditStu;

import java.util.List;

/**
 * @author gwj
 * @create 2024/10/16 19:31
 */
public interface TeacherService {
    Result<Page<LessonStu>> auditList(Integer pageNum, Integer pageSize);

    Result<AuditStu> audit(Long lessonStuId, Integer choose);

    Result<String> uploadBig(MultipartFile file, Integer chunk, Integer total, String md5);

    Result<String> mergePart(String md5, String fileName);

    Result<List<Integer>> getNoUp(String md5);
}
