package team.weilai.studythrough.pojo.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author gwj
 * @create 2024/10/10 20:47
 */
@Data
public class LessonDTO {
    private String lessonName;
    private MultipartFile file;
    private String end;
}
