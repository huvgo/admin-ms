package com.company.project.component.annotation;

import java.lang.annotation.*;

/**
 * 方法执行权限 注解
 */
@Target({ElementType.METHOD})// 注解作用在方法上
@Retention(RetentionPolicy.RUNTIME)// 注解的生命周期一直程序运行时都存在VM运行期间保留注解，可以通过反射机制读取注解信息*/
@Documented// 注解包含在Javadoc中
public @interface Permission {
}
