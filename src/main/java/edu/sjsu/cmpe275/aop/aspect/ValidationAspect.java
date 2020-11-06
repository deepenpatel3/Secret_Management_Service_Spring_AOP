package edu.sjsu.cmpe275.aop.aspect;

import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

@Aspect
@Order(1)
public class ValidationAspect {
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..)) && args(userId, secretContent)")
	public void checkArgsBeforeCreatingSecret(JoinPoint joinPoint, String userId, String secretContent) {
		System.out.println("Validating arguments for createSecret\n");
		if ( userId == null || secretContent == null || secretContent == "" ) {
			throw new IllegalArgumentException("Invalid parameters used for creating secret. Either userId or Secret-Content is null or empty"); 
		}
		if ( secretContent.length() > 100 ) {
			throw new IllegalArgumentException("Invalid parameters used for creating secret. Secret-content is more than 100 characters long."); 
		}
		
	}
	
	@Before("execution(public String edu.sjsu.cmpe275.aop.SecretService.readSecret(..)) && args(userId, secretId)")
	public void checkArgsBeforeReadingSecret(JoinPoint joinPoint, String userId, UUID secretId) {
		System.out.println("Validating arguments for readSecret\n");
		if ( userId == null || userId == "" || secretId == null ) {
			throw new IllegalArgumentException("Invalid parameters used for reading secret. Either userId or secretId is null or empty."); 
		}
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.shareSecret(..)) && args(userId,secretId,targetUserId)")
	public void checkArgsBeforeSharingSecret(JoinPoint joinPoint, String userId, UUID secretId, String targetUserId) {
		System.out.println("Validating arguments for shareSecret\n");
		if ( userId == null || secretId == null || targetUserId == null ) {
			throw new IllegalArgumentException("Invalid parameters used for sharing secret. Either userId or secretId or targetUserId is null "); 
		}
		if ( userId.equals(targetUserId)) {
			throw new IllegalArgumentException("Invalid targetUserId used for sharing secret. "+userId+ " cannot share secret with himself. "); 
		}
	}
	
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..)) && args(userId,secretId,targetUserId)")
	public void checkArgsBeforeUnsharingSecret(JoinPoint joinPoint, String userId, UUID secretId, String targetUserId) {
		System.out.println("Validating arguments for unshareSecret\n");
		if ( userId == null || secretId == null || targetUserId == null) {
			throw new IllegalArgumentException("Invalid parameters used for unsharing secret. Either userId or secretId or targetUserId is null "); 
		}
		if ( userId.equals(targetUserId)) {
			throw new IllegalArgumentException("Invalid targetUserId used for unsharing secret. "+userId+ " cannot unshare secret with himself. "); 
		}
	}
}
