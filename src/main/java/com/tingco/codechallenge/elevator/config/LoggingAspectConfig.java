package com.tingco.codechallenge.elevator.config;

import com.google.common.eventbus.EventBus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Lorinc Sonnevend
 */
@Aspect
@Component
class LoggingAspectConfig {

    private final EventBus eventBus;

    @Autowired
    public LoggingAspectConfig(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Pointcut("within(com.tingco.codechallenge.elevator.service..*) || within(com.tingco.codechallenge.elevator.controller..*)")
    public void allServicePackage() {
    }


    @Before("allServicePackage()")
    public void beforeInfoLogger(JoinPoint jp) {
        Logger logger = getLoggerByJointPoint(jp);

        if (logger.isInfoEnabled()) {
            Method method = getMethod(jp);
            logger.info("Entering method '{}' with arguments '{}'", method.getName(), Arrays.asList(jp.getArgs()));
            eventBus.post(String.format("Entering method %s with arguments %s", method.getName(), Arrays.asList(jp.getArgs())));
        }
    }

    @AfterReturning(pointcut = "allServicePackage()", returning = "retVal")
    public void afterReturningInfoLogger(JoinPoint jp, Object retVal) {
        Logger logger = getLoggerByJointPoint(jp);

        if (logger.isInfoEnabled()) {
            Method method = getMethod(jp);
            logger.info("Leaving method '{}' with return value '{}'", method.getName(), retVal);
            eventBus.post(String.format("Leaving method %s with return value %s", method.getName(), retVal));
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