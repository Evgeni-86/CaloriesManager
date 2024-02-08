package ru.caloriesmanager.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.caloriesmanager.web.SecurityUtil;

@Component
@Aspect
public class LoggerAspect {
    @Pointcut("execution(* ru.caloriesmanager.web.meal.*.getAll())")
    private void methodGetAllFromPackageWeb(){}

    @Pointcut("execution(* ru.caloriesmanager.web.meal.*.get())")
    private void methodGetFromPackageWeb(){}

    @Before("methodGetAllFromPackageWeb() || methodGetFromPackageWeb()")
    public void beforeGetAllAndGetMethods(){
    }
}
