/**
 * Created at 29-Apr-2016 by sabuj.das
 */
package com.consolefire.orm;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author sabuj.das
 *
 */
public final class ContextAutowireFactory implements ApplicationContextAware {

    private static ContextAutowireFactory instance;
    private ApplicationContext applicationContext;

    /**
     * 
     */
    private ContextAutowireFactory() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @return the factory instance
     */
    public static ContextAutowireFactory getInstance() {
        if(null == instance) {
            synchronized (ContextAutowireFactory.class) {
                if(null == instance) {
                    instance = new ContextAutowireFactory();
                }
            }
        }
        return instance;
    }
    
    public static void injectBean(final Object beanClass, Object... beans) {
        for (Object bean : beans) {
            if (bean == null) {
                getInstance().applicationContext.getAutowireCapableBeanFactory().autowireBean(beanClass);
                return;
            }
        }
    }

}
