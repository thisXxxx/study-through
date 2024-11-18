package team.weilai.studythrough.service;

import team.weilai.studythrough.pojo.exam.Repo;
import com.baomidou.mybatisplus.extension.service.IService;
import team.weilai.studythrough.pojo.vo.Result;

import java.util.List;

/**
* @author 86159
* @description 针对表【repo】的数据库操作Service
* @createDate 2024-11-11 21:52:30
*/
public interface RepoService extends IService<Repo> {

    Result<Void> add(List<Long> ids, Long repoId);
}
