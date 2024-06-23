package ru.tsu.hits.hitsinternship.logging;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private static String getRequestString() {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return "%s %s".formatted(request.getMethod(), request.getRequestURI());
    }

    @Pointcut("execution(public * ru.tsu.hits.hitsinternship..*Controller.*(..)))")
    public void apiMethodPointcut() {
    }

    @Nullable
    @Around("apiMethodPointcut()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {

        String requestString = getRequestString();
        String parametersString = getParametersString(pjp);

        log.info("Started processing request: {}, with params: {}", requestString, parametersString);

        var startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        var executionTime = System.currentTimeMillis() - startTime;

        log.info("Finished processing request: {} in {} ms, result: {}", requestString, executionTime, result);

        return result;
    }

    private String getParametersString(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

        String[] parameterNames = methodSignature.getParameterNames();
        Object[] parameterValues = pjp.getArgs();

        StringBuilder parameters = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            parameters.append(parameterNames[i]).append("=").append(parameterValues[i]);
            if (i < parameterNames.length - 1) {
                parameters.append(", ");
            }
        }

        return parameters.toString();
    }

}