package com.company.project.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<?> errorHandler(Exception ex){
        Result<?> fail = Results.Fail;

        //判断异常的类型,返回不一样的返回值
        if(ex instanceof ServiceException){
            ServiceException serviceException = (ServiceException) ex;
            return serviceException.getResult();
        } else{
            log.error(ex.getMessage(), ex);
        }
        return fail;
    }
}
