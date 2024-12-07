package team.weilai.studythrough.module.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import team.weilai.studythrough.mapper.exam.QuestionRepoMapper;
import team.weilai.studythrough.pojo.exam.main.QuestionRepo;
import team.weilai.studythrough.pojo.exam.main.Repo;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.module.exam.service.RepoService;
import team.weilai.studythrough.mapper.exam.RepoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86159
* @description 针对表【repo】的数据库操作Service实现
* @createDate 2024-11-11 21:52:30
*/
@Service
public class RepoServiceImpl extends ServiceImpl<RepoMapper, Repo>
    implements RepoService{

    @Resource
    private QuestionRepoMapper questionRepoMapper;


    @Override
    public Result<Void> add(List<Long> ids, Long repoId) {
        List<QuestionRepo> list = ids.stream().map(id ->
             new QuestionRepo(id, repoId)
        ).collect(Collectors.toList());
        questionRepoMapper.insert(list);
        return Result.ok();
    }
}




