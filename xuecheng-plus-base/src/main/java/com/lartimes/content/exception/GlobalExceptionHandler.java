package com.lartimes.content.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/8 10:36
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static RestErrorResponse getRestErrorResponse(XueChengPlusException e) {
        return new RestErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(XueChengPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(XueChengPlusException e) {
        log.error("【系统异常】{}", e.getMessage(), e);
        return getRestErrorResponse(e);
    }

    /**
     * 自定义参数异常 JSR-303  参数校验不成功，自动抛BindException
     */
    @ExceptionHandler(BindException.class)
    public String validatedBindException(BindException e) {
        String message = e.getAllErrors().get(0).getDefaultMessage();
        throw  new XueChengPlusException(message);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {

        log.error("【系统异常】{}", e.getMessage(), e);

        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());

    }
}

