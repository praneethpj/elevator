package com.tingco.codechallenge.elevator.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Lorinc Sonnevend
 */
@Aspect
@Component
class LoggingAspectConfig {

    @Pointcut("within(com.tingco.codechallenge.elevator.service..*)")
    public void allServicePackage() {
    }

    @Before("allServicePackage()")
    public void beforeInfoLogger(JoinPoint jp) {
        Logger logger = getLoggerByJointPoint(jp);

        if (logger.isInfoEnabled()) {
            Method method = getMethod(jp);
            logger.info("Entering method '{}' with arguments '{}'", method.getName(), Arrays.asList(jp.getArgs()));
        }
    }

    @AfterReturning(pointcut = "allServicePackage()", returning = "retVal")
    public void afterReturningInfoLogger(JoinPoint jp, Object retVal) {
        Logger logger = getLoggerByJointPoint(jp);

        if (logger.isInfoEnabled()) {
            Method method = getMethod(jp);
            logger.info("Leaving method '{}' with return value '{}'", method.getName(), retVal);
        }
    }

    private Logger getLoggerByJointPoint(JoinPoint jp) {
        return LoggerFactory.getLogger(jp.getSignature().getDeclaringType());
    }

    private Method getMethod(JoinPoint jp) {
        MethodSignature ms = (MethodSignature) jp.getSignature();
        return ms.getMethod();
    }

}