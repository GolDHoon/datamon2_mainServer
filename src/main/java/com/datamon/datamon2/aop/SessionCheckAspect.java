package com.datamon.datamon2.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class SessionCheckAspect {
    @Before("execution(* com.datamon.datamon2.controller.*.*.*(..))" +
            "&& !execution(* com.datamon.datamon2.controller.LandingPageController.*(..))" +
            "&& !execution(* com.datamon.datamon2.controller.*.UserSignController.login(..))")
    public void sessionCheck(){

    }
}
