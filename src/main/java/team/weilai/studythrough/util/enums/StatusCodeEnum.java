package team.weilai.studythrough.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCodeEnum {
    /**
     * 成功
     */
    SUCCESS(20000, "操作成功"),

    UPLOAD_SUCCESS(21000, "上传成功"),

    DATA_EPENDENCY(400007, "存在数据依赖，请确保该数据不被其他数据使用后再进行删除"),

    NO_DATA(400006, "无数据"),
    /**
     * 未登录
     */
    NO_LOGIN(401, "用户未登录"),
    LOGIN_ERROR(401,"登录失败"),
    /**
     * 没有操作权限
     */
    AUTHORIZED(403, "没有操作权限"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000, "系统异常"),
    /**
     * 失败
     */
    FAIL(51000, "操作失败"),
    /**
     * 参数校验失败
     */
    VALID_ERROR(52000, "参数格式不正确"),
    /**
     * 参数空值异常
     */
    NULL_VALID_ERROR(52000, "参数空值异常"),
    /**
     * 用户名已存在
     */
    USERNAME_EXIST(52001, "用户名已存在"),
    /**
     * 用户名不存在
     */
    USERNAME_NOT_EXIST(52002, "用户名不存在"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(52002, "用户不存在"),
    /**
     * 账号登录错误
     */
    ACCOUNT_LOGIN_ERROR(52002, "账号登录错误"),
    /**
     * 数据库中已存在相同数据
     */
    DATA_EXIST(52003, "数据已存在"),
    /**
     * qq登录错误
     */
    WEIBO_LOGIN_ERROR(53002, "微博登录错误"),
    //    DATA_EXIST(53003, "数据已存在"),
    DATA_EMPTY(53004, "数据不存在");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

}
