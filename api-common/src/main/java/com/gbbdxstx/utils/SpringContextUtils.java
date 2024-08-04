package com.gbbdxstx.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Spring 上下文获取工具
 *
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 通过名称获取 Bean
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 通过 class 获取 Bean
     *
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    /**
     * 通过名称和类型获取 Bean
     *
     * @param beanName
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }

    /**
     * 注册单例Bean
     * @param beanName
     * @param singletonObject
     */
    public static void registerSingleton(String beanName, Object singletonObject) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        beanFactory.registerSingleton(beanName, singletonObject);
    }

    /**
     * 销毁实例
     * @param beanName
     */
    public static void removeSingleton(String beanName) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        if (beanFactory.containsSingleton(beanName)) {
            Object bean = beanFactory.getSingleton(beanName);
            beanFactory.destroySingleton(beanName);
            if (bean instanceof DisposableBean) {
                try {
                    ((DisposableBean) bean).destroy();
                } catch (Exception e) {
                    // 处理销毁时的异常
                }
            }
        }
    }
}