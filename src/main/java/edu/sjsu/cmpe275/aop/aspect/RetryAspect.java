package edu.sjsu.cmpe275.aop.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

@Aspect
//@Order(1)
@Order(0)
public class RetryAspect {
	
	@Around("execution(* edu.sjsu.cmpe275.aop.SecretService.*(..))")
	public Object dummyAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Retry aspect prior to the executuion of the method %s\n", joinPoint.getSignature().getName());
		Object result = null;
		try {
			result =  joinPoint.proceed();

		} catch (IOException e1) {
			System.out.println("Trying to connect again: 1st re-attempt");
			try {
				result =  joinPoint.proceed();
			} catch (IOException e2) {
				System.out.println("Trying to connect again: 2nd re-attempt");
				try {
					result =  joinPoint.proceed();
				} catch (IOException e3) {
					System.out.println("Couldn't make it in 3 attempts");
					throw new IOException("Network Error has occured. Aborted the execution of the method " +  joinPoint.getSignature().getName());
				}
			}
		}
		return result;
	}

}
