package com.company.project.core;

/**
 * 断言是否为非空，如果为 {@code 空} 抛出 {@code ServiceException} 异常<br>
 */
public class Assert {

    public static <T> void requireNonNull(T obj, String errorMsgTemplate) {
        if (obj == null)
            throw new ServiceException(errorMsgTemplate);
    }

    public static <T> void requireNonNull(T obj, ResultCode resultCode, String errorMsgTemplate) {
        if (obj == null)
            throw new ServiceException(resultCode, errorMsgTemplate);
    }

    public static void requireTrue(boolean expression, String errorMsgTemplate) {
        if (!expression)
            throw new ServiceException(errorMsgTemplate);
    }


    public static void requireTrue(boolean expression, ResultCode resultCode, String errorMsgTemplate) {
        if (!expression)
            throw new ServiceException(resultCode, errorMsgTemplate);
    }
}
