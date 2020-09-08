package com.company.project.core;

/**
 * 断言是否为非空，如果为 {@code 空} 抛出 {@code ServiceException} 异常<br>
 */
public class Assert {

    public static <T> void requireNonNull(T obj, Result<?> result) {
        if (obj == null)
            throw new ServiceException(result);
    }


    public static void requireTrue(boolean expression, Result<?> result) {
        if (!expression)
            throw new ServiceException(result);
    }

}
