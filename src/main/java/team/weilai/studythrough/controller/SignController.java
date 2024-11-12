package team.weilai.studythrough.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.weilai.studythrough.pojo.dto.SignDTO;
import team.weilai.studythrough.pojo.vo.Result;
import team.weilai.studythrough.service.SignService;
import team.weilai.studythrough.websocket.pojo.SignMsg;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author gwj
 * @date 2024/11/7 15:25
 */
@RestController
@RequestMapping("/sign")
@Api(tags = "签到")
public class SignController {

    @Resource
    private SignService signService;


    @PostMapping("/pubSign")
    @ApiOperation("发布签到")
    @PreAuthorize("hasAuthority('1')")
    public Result<SignMsg> pubSign(@Valid SignDTO signDTO) {
        return signService.pubSign(signDTO);
    }

    @PostMapping("/click")
    @ApiOperation("学生签到")
    @PreAuthorize("hasAuthority('0')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longitude",value = "经度"),
            @ApiImplicitParam(name = "latitude",value = "纬度")
    })
    public Result<Void> click(@NotNull Long signId,String longitude,String latitude) {
        return signService.click(signId,longitude,latitude);
    }
}
