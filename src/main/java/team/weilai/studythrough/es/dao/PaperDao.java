package team.weilai.studythrough.es.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import team.weilai.studythrough.pojo.exam.main.Paper;

/**
 * @author gwj
 * @date 2024/12/6 11:25
 */
public interface PaperDao extends ElasticsearchRepository<Paper,Long>,SelfPaperDao{
}
