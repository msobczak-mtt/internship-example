package travel.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class TravelAspect {

    @Before("execution(public * travel.impl..*(..))")
    void logEnteringMethod(JoinPoint joinPoint) {
        System.out.println("[TRACE] Entering Method: " + joinPoint.getSignature().toString()
        + " on " + joinPoint.getTarget().getClass().getName() + " with " + Arrays.toString(joinPoint.getArgs()));
    }

}
