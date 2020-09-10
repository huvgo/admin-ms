package com.company.project.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<?> errorHandler(Exception ex){
        //判断异常的类型,返回不一样的返回值
        if(ex instanceof ServiceException){
            ServiceException serviceException = (ServiceException) ex;
            return serviceException.getResult();
        } else if(ex instanceof MethodArgumentNotValidException){
            return Results.PARAM_ERROR;
        } else if(ex instanceof NullPointerException){
            // TODO
            log.error(ex.getMessage(), ex);
            return Results.Fail.setErrorMessage(ex.getMessage());
        } else{
            log.error(ex.getMessage(), ex);
            return Results.Fail.setErrorMessage(ex.getMessage());
        }
    }
}
