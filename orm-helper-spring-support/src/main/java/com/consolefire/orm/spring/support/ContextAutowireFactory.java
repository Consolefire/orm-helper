/**
 * Created at 29-Apr-2016 by sabuj.das
 */

package com.consolefire.orm.spring.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Inject bean dependency on-demand.
 * 
 * @author sabuj.das
 *
 */
public final class ContextAutowireFactory implements ApplicationContextAware {

    private static volatile ContextAutowireFactory instance;
    private ApplicationContext applicationContext;

    /**
     * Private constructor.
     */
    private ContextAutowireFactory() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.
     * context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Create instance of this factory.
     * 
     * @return the factory instance
     */
    public static ContextAutowireFactory getInstance() {
        if (null == instance) {
            synchronized (ContextAutowireFactory.class) {
                if (null == instance) {
                    instance = new ContextAutowireFactory();
                }
            }
        }
        return instance;
    }

    /**
     * Inject bean on-demand.
     * 
     * @param beanClass to create.
     * @param beans to inject.
     */
    public static void injectBean(final Object beanClass, Object... beans) {
        for (Object bean : beans) {
            if (bean == null) {
                getInstance().applicationContext.getAutowireCapableBeanFactory().autowireBean(beanClass);
                return;
            }
        }
    }

}
