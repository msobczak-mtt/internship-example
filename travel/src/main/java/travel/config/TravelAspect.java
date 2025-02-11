package travel.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class TravelAspect {

    @Pointcut("execution(public * travel.impl..*(..))")
    void allPublicInImpl(){}

    @Before("allPublicInImpl()")
    void logEnteringMethod(JoinPoint joinPoint) {
        System.out.println("[TRACE] Entering Method: " + joinPoint.getSignature().toString()
        + " on " + joinPoint.getTarget().getClass().getName() + " with " + Arrays.toString(joinPoint.getArgs()));
    }

    @After("allPublicInImpl()")
    void logExitingMethod(JoinPoint joinPoint) {
        System.out.println("[TRACE] Exiting Method: " + joinPoint.getSignature().toString()
                + " on " + joinPoint.getTarget().getClass().getName() + " with " + Arrays.toString(joinPoint.getArgs()));
    }

}
