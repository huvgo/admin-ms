package com.company.project.component.annotation;

import java.lang.annotation.*;

/**
 * 方法记录日志 注解
 * **只能放在controller类下的方法上**
 */
@Target({ElementType.METHOD})// 注解作用在方法上
@Retention(RetentionPolicy.RUNTIME)// 注解的生命周期一直程序运行时都存在VM运行期间保留注解，可以通过反射机制读取注解信息*/
@Documented// 注解包含在Javadoc中
public @interface SaveLog {
}