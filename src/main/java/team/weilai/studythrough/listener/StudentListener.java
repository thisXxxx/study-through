package team.weilai.studythrough.listener;

import cn.hutool.core.lang.UUID;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.weilai.studythrough.pojo.User;
import team.weilai.studythrough.service.UserService;

import java.util.List;


/**
 * @author gwj
 * @since 2024/5/17 9:06
 */
// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class StudentListener implements ReadListener<User> {


    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;



    /**
     * 缓存的数据
     */
    private List<User> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


    private UserService userService;
    private Integer type;


    public StudentListener(UserService userService,Integer type) {
        this.userService = userService;
        this.type = type;
    }


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param user    one row value. It is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(User user, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(user));
        if (isExist(user.getUsername())) {
            log.error("数据已存在");
        }else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encode = encoder.encode("123456");
            user.setPassword(encode);
            user.setStatus(type);
            cachedDataList.add(user);
        }

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }


    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }


    /**
     * 加上存储数据库
     */
    private void saveData() {

        log.info("{}条数据，开始存储数据库！", cachedDataList.size());

        userService.saveBatch(cachedDataList);

        log.info("存储数据库成功！");

    }

    private boolean isExist(String username) {
        User one = userService.query().eq("usrename", username).one();
        return one != null;
    }



}
