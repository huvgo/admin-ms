package com.company.project.core;

/**
 * 断言是否为非空，如果为 {@code 空} 抛出 {@code ServiceException} 异常<br>
 */
public class Assert {

    public static <T> void requireNonNull(T obj, String userTips) {
        if (obj == null)
            throw new ServiceException(Result.warning(userTips));
    }


    public static void requireTrue(boolean expression, String userTips) {
        if (!expression)
            throw new ServiceException(Result.warning(userTips));
    }

}
