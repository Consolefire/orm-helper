package com.consolefire.orm.spring.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.consolefire.orm.common.timed.TimeTrack;

/**
 * Created by sabuj.das on 31/05/16.
 */
@Aspect
@Component
public class TimeTrackerInterceptor implements Ordered {

    private static Logger logger = LoggerFactory.getLogger(TimeTrackerInterceptor.class);

    private int order;

    @Override
    public int getOrder() {
        return this.order;
    }

    @Value("21")
    public void setOrder(int order) {
        this.order = order;
    }

    @Around("@annotation(timeTrack)")
    public Object proceed(ProceedingJoinPoint pjp, TimeTrack timeTrack) throws Throwable {
        logger.info("*********************** Intercepting TimeTrack **********************");
        try {
            if (null != timeTrack) {
                Level level = timeTrack.logLevel();
                Long startTime = System.currentTimeMillis();
                Object result = pjp.proceed();
                Long endTime = System.currentTimeMillis();
                writeLog(pjp, level, startTime, endTime);
                return result;
            } else {
                if(logger.isDebugEnabled()){
                    logger.debug("Method is not marked for TimeTrack");
                }
                return pjp.proceed();
            }
        } finally {
            logger.info("*********************** TimeTrack Interception Completed **********************");
        }

    }

    private void writeLog(final ProceedingJoinPoint pjp,
                          final Level level,
                          final Long startTime, final Long endTime) {
        switch (level){
            case DEBUG:
                if(logger.isDebugEnabled()) {
                    logger.debug("Time taken for method: [{}.{}] is: [{}] ms",
                            pjp.getSignature().getDeclaringTypeName(),
                            pjp.getSignature().getName(),
                            (endTime-startTime));
                }
                break;
            case INFO:
                if(logger.isInfoEnabled()) {
                    logger.info("Time taken for method: [{}.{}] is: [{}] ms",
                            pjp.getSignature().getDeclaringTypeName(),
                            pjp.getSignature().getName(),
                            (endTime-startTime));
                }
                break;
            case ERROR:
                if(logger.isErrorEnabled()) {
                    logger.error("Time taken for method: [{}.{}] is: [{}] ms",
                            pjp.getSignature().getDeclaringTypeName(),
                            pjp.getSignature().getName(),
                            (endTime-startTime));
                }
                break;
            case WARN:
                if(logger.isWarnEnabled()) {
                    logger.warn("Time taken for method: [{}.{}] is: [{}] ms",
                            pjp.getSignature().getDeclaringTypeName(),
                            pjp.getSignature().getName(),
                            (endTime-startTime));
                }
                break;
            case TRACE:
                if(logger.isTraceEnabled()) {
                    logger.trace("Time taken for method: [{}.{}] is: [{}] ms",
                            pjp.getSignature().getDeclaringTypeName(),
                            pjp.getSignature().getName(),
                            (endTime-startTime));
                }
                break;
        }
    }
}
