package team.weilai.studythrough.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.weilai.studythrough.pojo.DTO.LoginDTO;
import team.weilai.studythrough.pojo.VO.Result;
import team.weilai.studythrough.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author gwj
 * @create 2024/10/9 17:58
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/login")
    @ApiOperation("登录")
    @PreAuthorize("permitAll()")
    public Result<Map<String,String>> login(@Valid @RequestBody LoginDTO loginDTO) {

        return userService.login(loginDTO);
    }

}
