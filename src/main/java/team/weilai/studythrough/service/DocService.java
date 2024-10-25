package team.weilai.studythrough.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.dto.FileDTO;
import team.weilai.studythrough.pojo.Doc;
import team.weilai.studythrough.pojo.vo.Result;

/**
 * @author gwj
 * @create 2024/10/11 17:06
 */
public interface DocService extends IService<Doc> {
    Result<Void> makeDir(Long parentId, Long lessonId,String dirName);

    Result<Void> makeFile(String filePath,String fileName, Long parentId, Long lessonId);

    Result<Page<Doc>> listFile(FileDTO fileDTO);

}
