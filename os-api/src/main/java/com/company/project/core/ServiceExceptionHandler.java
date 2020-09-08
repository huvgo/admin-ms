package com.company.project.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<?> errorHandler(Exception ex) {
        Result<?> fail = Results.Fail;

        //判断异常的类型,返回不一样的返回值
        if (ex instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) ex;
            return serviceException.getResult();
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            List<String> allErrorMessage = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return Results.PARAM_ERROR.setData(allErrorMessage);
        } else if (ex instanceof NullPointerException) {
            // TODO
            log.error(ex.getMessage(), ex);
            fail.setErrorMessage(ex.getMessage());
        } else {
            log.error(ex.getMessage(), ex);
            fail.setErrorMessage(ex.getMessage());
        }
        return fail;
    }
}
