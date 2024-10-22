package team.weilai.studythrough.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.mapper.DocMapper;
import team.weilai.studythrough.pojo.DTO.FileDTO;
import team.weilai.studythrough.pojo.Doc;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.DocService;
import team.weilai.studythrough.util.MinioUtil;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author gwj
 * @create 2024/10/11 17:07
 */
@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements DocService {

    @Resource
    private MinioUtil minioUtil;
    @Resource
    private DocMapper docMapper;

    @Override
    public Result<Void> makeDir(Long parentId, Long lessonId, String dirName) {
        Doc doc = new Doc(lessonId, dirName);
        if (parentId != -1) {
            Doc one = query()
                    .select("lesson_id")
                    .eq("doc_id", parentId).eq("is_dir", 1).one();
            if (one == null || !Objects.equals(one.getLessonId(), lessonId)) {
                return Result.fail(StatusCodeEnum.DATA_EMPTY);
            }
            doc.setParentId(parentId);
        }
        save(doc);
        return Result.ok();
    }

    @Override
    public Result<Void> makeFile(String filePath,String fileName, Long parentId, Long lessonId) {
        Doc doc = new Doc(lessonId, fileName, filePath);
        if (parentId != -1) {
            Doc one = query()
                    .select("lesson_id")
                    .eq("doc_id", parentId).eq("is_dir", 1).one();
            if (one == null || !Objects.equals(one.getLessonId(), lessonId)) {
                return Result.fail(StatusCodeEnum.DATA_EMPTY);
            }
            doc.setParentId(parentId);
        }
        doc.setIsDir(0);
        save(doc);
        return Result.ok();
    }

    @Override
    public Result<Page<Doc>> listFile(FileDTO fileDTO) {
        Page<Doc> page = new Page<>(fileDTO.getPageNum(), fileDTO.getPageSize());
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        String title = fileDTO.getDocTitle();
        Long parentId = fileDTO.getParentId();
        wrapper.eq("lesson_id", fileDTO.getLessonId()).eq("parent_id", parentId);
        if (title != null && !title.trim().isEmpty()) {
            wrapper.like("doc_title", title);
        }
        docMapper.selectPage(page, wrapper);
        return Result.ok(page);
    }
}
