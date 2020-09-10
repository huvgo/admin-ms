package com.company.project.component.plugin;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.websocket.server.ServerEndpointConfig;

/**
 * 扩展了ServerEndpoint的Configurator
 *
 * 为了解决springboot websocket 连接时，如果设置为SpringConfigurator，如下所示：
 *
 * \\ @ServerEndpoint(value = “/websocket/test”,configurator = SpringConfigurator.class)
 * 当接收数据的时候，就会报错：
 * Failed to find the root WebApplicationContext. Was ContextLoaderListener not used?
 */
public class CustomSpringConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

    /**
     * Spring application context.
     */
    private static volatile BeanFactory context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CustomSpringConfigurator.context = applicationContext;
    }
}