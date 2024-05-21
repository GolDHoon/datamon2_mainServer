package com.datamon.datamon2.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopConfigure {
    @Pointcut("execution(* com.datamon.datamon2.controller..*.*(..))")
    private void anyControllerMethod() {}

    @Pointcut("execution(* com.datamon.datamon2.controller.UserSignController.*(..))")
    private void anyUserSignControllerMethod() {}

    @Before("anyControllerMethod() && !anyUserSignControllerMethod()")
    public void beforeAdvice(){
        System.out.println("Before method execution");
    }
}
