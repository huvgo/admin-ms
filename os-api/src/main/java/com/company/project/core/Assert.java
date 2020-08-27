package com.company.project.core;

/**
 * 断言是否为非空，如果为 {@code 空} 抛出 {@code ServiceException} 异常<br>
 */
public class Assert {

    public static <T> T requireNonNull(T obj, String errorMsgTemplate) {
        if (obj == null)
            throw new ServiceException(errorMsgTemplate);
        return obj;
    }

    public static <T> T requireNonNull(T obj, ResultCode resultCode, String errorMsgTemplate) {
        if (obj == null)
            throw new ServiceException(resultCode, errorMsgTemplate);
        return obj;
    }

    public static void requireTrue(boolean expression, String errorMsgTemplate) {
        if (!expression) {
            throw new ServiceException(errorMsgTemplate);
        }
    }


    public static void requireTrue(boolean expression, ResultCode resultCode, String errorMsgTemplate) {
        if (!expression) {
            throw new ServiceException(resultCode, errorMsgTemplate);
        }
    }
}
