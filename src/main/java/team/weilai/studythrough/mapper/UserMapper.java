package team.weilai.studythrough.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.weilai.studythrough.pojo.User;
import team.weilai.studythrough.pojo.vo.UserVO;

/**
 * @author gwj
 * @create 2024/10/9 17:48
 */
public interface UserMapper extends BaseMapper<User> {
    Page<UserVO> selectUsers(@Param("page") Page<UserVO> page,
                             @Param("name")String name,
                             @Param("username")String username,
                             @Param("status")Integer status);
}
