package com.lewis.core.exception;

import com.lewis.core.constant.HttpStatus;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 *
 * @author Lewis
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public BaseResult baseException(BaseException e) {
        return BaseResult.fail(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public BaseResult businessException(CustomException e) {
        if (StringUtils.isNull(e.getCode())) {
            return BaseResult.fail(e.getMessage());
        }
        return BaseResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseResult handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return BaseResult.fail(HttpStatus.NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public BaseResult handleAuthorizationException(AccessDeniedException e) {
        log.error(e.getMessage());
        return BaseResult.fail(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public BaseResult handleAccountExpiredException(AccountExpiredException e) {
        log.error(e.getMessage(), e);
        return BaseResult.fail(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public BaseResult handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return BaseResult.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return BaseResult.fail(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public BaseResult validatedBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return BaseResult.fail(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return BaseResult.fail(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public BaseResult demoModeException(DemoModeException e) {
        return BaseResult.fail("演示模式，不允许操作");
    }
}
