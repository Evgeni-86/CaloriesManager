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

    @Before("methodGetAllFromPackageWeb()")
    public void beforeGetAllAdvice(JoinPoint joinPoint) {
        System.out.println(joinPoint.getTarget().getClass());
        System.out.printf("TEST AOP : getAll for user %s\n", SecurityUtil.authUserId());
    }

//    @Before("execution(* ru.caloriemanager.web.meal.*.getAll())")
//    public void beforeGetAllAdvice(JoinPoint joinPoint) {
//        System.out.println(joinPoint.getTarget().getClass());
//        System.out.printf("TEST AOP : getAll for user %s\n", SecurityUtil.authUserId());
//    }

    public void beforeGetAdvice(JoinPoint joinPoint, int id) {
        System.out.println(joinPoint.getTarget().getClass());
        System.out.printf("TEST AOP : get meal %s\n", id);
    }
}
