package it.academy.cursebackspring.components;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut(value = "execution(public * it.academy.cursebackspring.services.*.*(..))")
    private void publicMethodsFromServiceLayer() {

    }

    @Around(value = "publicMethodsFromServiceLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> Executing: {}() - {}", methodName, Arrays.toString(args));
        try {
            Object result = joinPoint.proceed();
            log.info("<< Method successfully end: {}() - {}", methodName, result);
            return result;
        }catch (Throwable e) {
            log.error("Error: {}", e.getLocalizedMessage(), e);
            throw e;
        }
    }

}
