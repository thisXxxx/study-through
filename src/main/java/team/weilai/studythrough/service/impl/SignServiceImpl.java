package team.weilai.studythrough.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.weilai.studythrough.constants.Constants;
import team.weilai.studythrough.mapper.SignMapper;
import team.weilai.studythrough.mapper.SignStuMapper;
import team.weilai.studythrough.pojo.Sign;
import team.weilai.studythrough.pojo.SignStu;
import team.weilai.studythrough.pojo.dto.SignDTO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.SignService;
import team.weilai.studythrough.util.CommonUtils;
import team.weilai.studythrough.util.HaversineUtil;
import team.weilai.studythrough.websocket.pojo.SignMsg;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static team.weilai.studythrough.constants.Constants.*;
import static team.weilai.studythrough.constants.RedisConstants.SIGN;

/**
 * @author gwj
 * @date 2024/11/7 15:40
 */
@Service
@Slf4j
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements SignService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private SignStuMapper signStuMapper;

    @Override
    public Result<SignMsg> pubSign(SignDTO signDTO) {
        Sign sign = BeanUtil.copyProperties(signDTO, Sign.class);
        save(sign);
        Map<String,Object> map = new HashMap<>();
        map.put("longitude",sign.getLongitude());
        map.put("latitude",sign.getLatitude());
        map.put("distance",sign.getDistance());
        String key = SIGN + sign.getSignId();
        redisTemplate.opsForHash().putAll(key,map);
        redisTemplate.expire(key,sign.getKeepTime(), TimeUnit.MINUTES);
        SignMsg signMsg = new SignMsg(sign.getSignId(), sign.getKeepTime());
        return Result.ok(signMsg);
    }

    @Override
    public Result<Void> click(Long signId, String longitude, String latitude) {
        String key = SIGN + signId;
        Long userId = CommonUtils.getUserId();
        String setLong = (String) redisTemplate.opsForHash().get(key, "longitude");
        String setLa = (String) redisTemplate.opsForHash().get(key, "latitude");
        String s = (String) redisTemplate.opsForHash().get(key, "distance");
        if (setLa == null || setLong == null || s == null) {
            add(userId,signId, TIME_EXCEED);
            return Result.fail("超时");
        }
        int distance = Integer.parseInt(s);
        double haversine = HaversineUtil.haversine(Double.parseDouble(latitude), Double.parseDouble(longitude),
                Double.parseDouble(setLa), Double.parseDouble(setLong));
        if (haversine > distance) {
            add(userId,signId,DISTANT_EXCEED);
            return Result.fail("不在指定区域签到");
        }
        add(userId,signId,SUCCESS);
        return Result.ok();
    }

    //异步存入数据库
    @Override
    public void add(Long userId, Long signId, Integer status) {
        int i = signStuMapper.insert(new SignStu(signId, userId, status));
        if (i == 0) {
            log.error("存储失败，【用户】{}，【签到】{}，【状态】{}",userId,signId,status);
        }
    }
}
