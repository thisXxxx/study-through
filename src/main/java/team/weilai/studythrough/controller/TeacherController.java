package team.weilai.studythrough.controller;

import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gwj
 * @create 2024/10/9 21:26
 */
@RestController
@RequestMapping("/tea")
@Api(tags = "老师")
@PreAuthorize("hasAuthority('1')")
public class TeacherController {
}
