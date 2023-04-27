package com.gsg.blog.ex;

import com.gsg.blog.utils.BaseUtil;
import com.gsg.blog.utils.R;
import com.gsg.blog.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常返回处理
 * @author shuaigang
 * @date  2023/3/2 14:57
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ResponseBody
    @ExceptionHandler
    public Result<?> handlerException(Exception e){
        log.error("系统异常",e);
        return Result.ok(BaseUtil.encode(R.failed(e)));
    }

    @ResponseBody
    @ExceptionHandler
    public Result<?> handlerServiceException(ServiceException e){
        log.error("业务异常",e);
        return Result.ok(BaseUtil.encode(R.failed(e)));
    }

    @ResponseBody
    @ExceptionHandler
    public Result<?> missingServiceException(MissingServletRequestParameterException e){
        log.error("requestParam异常",e);
        return Result.ok(BaseUtil.encode(R.failed(e)));
    }

    @ResponseBody
    @ExceptionHandler
    public Result<?> validServiceException(MethodArgumentNotValidException e){
        log.error("参数校验异常",e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        return Result.ok(BaseUtil.encode(R.failed(allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";")))));
    }
    @ResponseBody
    @ExceptionHandler
    public Result<?> nullPointerServiceException(NullPointerException e){
        log.error("空指针异常",e);
        return Result.ok(BaseUtil.encode(R.failed(e)));
    }

    @ResponseBody
    @ExceptionHandler
    public Result<?> sqlServiceException(SQLException e){
        log.error("数据库异常",e);
        return Result.ok(BaseUtil.encode(R.failed("数据库异常，请联系管理员！")));
    }

    @ExceptionHandler
    public Result<?> handlerDeniedException(AccessDeniedException e) {
        log.error("业务异常", e);
        return Result.ok(BaseUtil.encode(R.failed(e.getMessage())));
    }

    /**
     * 自定义错误拦截
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> illegalArgumentExceptionHandler(IllegalArgumentException exception) {
        log.error("非法参数异常", exception);
        return Result.ok(BaseUtil.encode(R.failed(R.ERROR_PARAMS, exception.getMessage())));
    }

}
