package com.company.project.common;

import com.company.project.core.Result;
import com.company.project.core.ServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<?> errorHandler(Exception ex) {
        Result<?> fail = Result.fail();
        //判断异常的类型,返回不一样的返回值
        if (ex instanceof ServiceException) {
            String message = ex.getMessage();
            fail.setMessage(message);
        } else {
            ex.printStackTrace();
        }
        return fail;
    }
}
