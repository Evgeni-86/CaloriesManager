package ru.caloriemanager.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.caloriemanager.web.SecurityUtil;

@Component
@Aspect
public class LoggerAspect {
    @Pointcut("execution(* ru.caloriemanager.web.meal.*.getAll())")
    private void methodGetAllFromPackageWeb(){}

    @Pointcut("execution(* ru.caloriemanager.web.meal.*.get())")
    private void methodGetFromPackageWeb(){}

    @Before("methodGetAllFromPackageWeb() || methodGetFromPackageWeb()")
    public void beforeGetAllAndGetMethods(){
    }
}
