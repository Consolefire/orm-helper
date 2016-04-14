package com.consolefire.orm.helper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class MasterSlaveTransactionInterceptor implements Ordered {

    private static Logger logger = LoggerFactory.getLogger(MasterSlaveTransactionInterceptor.class);
    private int order;

    @Override
    public int getOrder() {
        return order;
    }

    @Value("20")
    public void setOrder(int order) {
        this.order = order;
    }


    @Around("@annotation(transactional)")
    public Object proceed(ProceedingJoinPoint pjp, Transactional transactional) throws Throwable {
        logger.info("*********************** Intercepting Transaction **********************");
        try {
            if (null != transactional) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Setting target DataSource MASTER / SLAVE");
                }
                if (transactional.readOnly()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Current transaction is RO");
                    }
                    DataSourceTypeContext.setDataSourceType(DataSourceTypeContext.DataSourceType.SLAVE);
                } else {
                    if(logger.isDebugEnabled()){
                        logger.debug("Current transaction is RW");
                    }
                    DataSourceTypeContext.setDataSourceType(DataSourceTypeContext.DataSourceType.MASTER);
                }
                if(logger.isDebugEnabled()){
                    logger.debug("Selected DataSource Type: "
                            + DataSourceTypeContext.getDataSourceType().name());
                    logger.debug("Executing target method ");
                }
                Object result = pjp.proceed();
                DataSourceTypeContext.clearDataSourceType();
                return result;
            } else {
                if(logger.isDebugEnabled()){
                    logger.debug("Method is not transactional");
                }
                return pjp.proceed();
            }
        } finally {
            DataSourceTypeContext.clearDataSourceType();
            logger.info("*********************** Transaction Interception Completed **********************");
        }

    }
}
