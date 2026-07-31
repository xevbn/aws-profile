package com.example.awsprofile.domain.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {
    //메서드 및 클래스에 적용 가능하도록 설정
    //@annotation으로 메서드에 어노테이션이 붙어있는지 확인
    //@within으로 클래스에 어노테이션이 붙어있는지 확인
    @Before("@annotation(com.example.awsprofile.domain.common.annotation.LogExecution) || @within(com.example.awsprofile.domain.common.annotation.LogExecution)")
    public void logExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.info("[API - LOG] {} 호출됨", methodName);
    }
}
