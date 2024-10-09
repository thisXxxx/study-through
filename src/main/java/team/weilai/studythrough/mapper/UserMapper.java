package team.weilai.studythrough.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.weilai.studythrough.pojo.User;

/**
 * @author gwj
 * @create 2024/10/9 17:48
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
