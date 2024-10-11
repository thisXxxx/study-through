package team.weilai.studythrough.handler;


import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.weilai.studythrough.enums.StatusCodeEnum;
import team.weilai.studythrough.pojo.VO.Result;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handle(IllegalArgumentException e) {
        return Result.fail(StatusCodeEnum.VALID_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handle(MethodArgumentNotValidException e) {
        return Result.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handle(BindException e) {
        return Result.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

}
