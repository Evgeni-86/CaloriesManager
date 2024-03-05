package ru.caloriesmanager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.caloriesmanager.repository.jpa.JpaMealRepositoryImpl;
import ru.caloriesmanager.repository.jpa.JpaUserRepositoryImpl;
import java.util.Arrays;
import java.util.stream.Collectors;


@Component
@Aspect
public class LoggerAspect {
    private static final Logger LOG_MEAL = LoggerFactory.getLogger(JpaMealRepositoryImpl.class);
    private static final Logger LOG_USER = LoggerFactory.getLogger(JpaUserRepositoryImpl.class);

    @Around("execution(* ru.caloriesmanager.repository.jpa.JpaMealRepositoryImpl.*(..))")
    public Object repositoryMealMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object resultMethod = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        LOG_MEAL.info("target class: {}; method signature: {}; arguments: {}; time: {} ms",
                proceedingJoinPoint.getTarget().toString(),
                proceedingJoinPoint.getSignature(),
                Arrays.stream(proceedingJoinPoint.getArgs()).map(Object::toString).collect(Collectors.toList()),
                endTime - startTime);

        return resultMethod;
    }

    @Around("execution(* ru.caloriesmanager.repository.jpa.JpaUserRepositoryImpl.*(..))")
    public Object repositoryUserMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object resultMethod = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        LOG_USER.info("target class: {}; method signature: {}; arguments: {}; time: {} ms",
                proceedingJoinPoint.getTarget().toString(),
                proceedingJoinPoint.getSignature(),
                Arrays.stream(proceedingJoinPoint.getArgs()).map(Object::toString).collect(Collectors.toList()),
                endTime - startTime);

        return resultMethod;
    }

    @Around("execution(* ru.caloriesmanager.service.MealService.*(..))")
    public Object serviceMealMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object resultMethod = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        LOG_MEAL.info("target class: {}; method signature: {}; arguments: {}; time: {} ms",
                proceedingJoinPoint.getTarget().toString(),
                proceedingJoinPoint.getSignature(),
                Arrays.stream(proceedingJoinPoint.getArgs()).map(Object::toString).collect(Collectors.toList()),
                endTime - startTime);

        return resultMethod;
    }
}
