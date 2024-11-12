package team.weilai.studythrough.service;

import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.pojo.vo.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gwj
 * @create 2024/10/9 21:41
 */
public interface AdminService {
    void getStuSample(HttpServletResponse response) throws IOException;

    Result<Void> uploadStu(MultipartFile file,Integer type);

    Result<Void> aiKnow(MultipartFile file);
}
