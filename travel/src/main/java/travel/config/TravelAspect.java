package travel.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import travel.Person;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Component
@Aspect
public class TravelAspect {

    @Pointcut("execution(public * travel.impl..*(..))")
    void allPublicInImpl(){}

    @Pointcut("execution(public * *(travel.Person))")
    void allAcceptingPerson(){}

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

    @Around("allAcceptingPerson()")
    Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        Person person = (Person) joinPoint.getArgs()[0];
        System.out.println("[TRACE] About to measure execution time of a method having person: " + person);

        Instant startTime = Instant.now();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        Instant endTime = Instant.now();
        System.out.println("[TRACE] Execution time of " + joinPoint.toLongString() + " is " + Duration.between(startTime, endTime));

        return result;
    }

}
