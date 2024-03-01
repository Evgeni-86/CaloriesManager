package ru.caloriesmanager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.repository.jpa.JpaMealRepositoryImpl;

@Component
@Aspect
public class LoggerAspect {
    private static final Logger LOG_MEAL = LoggerFactory.getLogger(JpaMealRepositoryImpl.class);

    @Around("execution(* ru.caloriesmanager.repository.jpa.JpaMealRepositoryImpl.save())")
    public Object aroundAllRepositoryMethods(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String methodName = methodSignature.getName();
        return null;
    }

    @Pointcut("execution(* ru.caloriesmanager.web.meal.*.getAll())")
    private void methodGetAllFromPackageWeb() {
    }

    @Pointcut("execution(* ru.caloriesmanager.web.meal.*.get())")
    private void methodGetFromPackageWeb() {
    }

    @Before("methodGetAllFromPackageWeb() || methodGetFromPackageWeb()")
    public void beforeGetAllAndGetMethods() {
    }
}
