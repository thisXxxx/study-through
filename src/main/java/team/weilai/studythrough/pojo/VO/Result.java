package team.weilai.studythrough.pojo.VO;

import lombok.Data;
import lombok.ToString;
import team.weilai.studythrough.enums.StatusCodeEnum;


@Data
@ToString
public class Result<T> {

    /**
     * 返回状态
     */
    private Boolean success;
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String errMsg;
    /**
     * 返回数据
     */
    private T data;

    public static <T> Result<T> ok() {
        return restResult(true, null, StatusCodeEnum.SUCCESS.getCode(),null);
    }

    public static <T> Result<T> ok(StatusCodeEnum statusCodeEnum) {
        return restResult(true, null, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> Result<T> ok(T data) {
        return restResult(true, data, StatusCodeEnum.SUCCESS.getCode(), null);
    }

    public static <T> Result<T> ok(T data, String message) {
        return restResult(true, data, StatusCodeEnum.SUCCESS.getCode(), message);
    }

    public static <T> Result<T> fail() {
        return restResult(false, null, StatusCodeEnum.FAIL.getCode(), StatusCodeEnum.FAIL.getDesc());
    }

    public static <T> Result<T> fail(StatusCodeEnum statusCodeEnum) {
        return restResult(false, null, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> Result<T> fail(String message) {
        return restResult(false, message);
    }

    public static <T> Result<T> fail(T data) {
        return restResult(false, data, StatusCodeEnum.FAIL.getCode(), StatusCodeEnum.FAIL.getDesc());
    }

    public static <T> Result<T> fail(T data, String message) {
        return restResult(false, data,StatusCodeEnum.FAIL.getCode(), message);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return restResult(false, null, code, message);
    }

    private static <T> Result<T> restResult(Boolean flag, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setSuccess(flag);
        apiResult.setCode(flag ? StatusCodeEnum.SUCCESS.getCode() : StatusCodeEnum.FAIL.getCode());
        apiResult.setErrMsg(message);
        return apiResult;
    }

    public static <T> Result<T> restResult(Boolean flag, T data, Integer code, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setSuccess(flag);
        apiResult.setData(data);
        apiResult.setCode(code);
        apiResult.setErrMsg(message);
        return apiResult;
    }

}
