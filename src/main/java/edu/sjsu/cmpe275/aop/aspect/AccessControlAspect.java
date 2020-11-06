package edu.sjsu.cmpe275.aop.aspect;

import java.io.IOException;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.SecretStatsImpl;
import edu.sjsu.cmpe275.aop.NotAuthorizedException;

@Aspect
//@Order(1)
@Order(2)
public class AccessControlAspect {
	@Autowired SecretStatsImpl stats;
	
	
	@Before("execution(public String edu.sjsu.cmpe275.aop.SecretService.readSecret(..)) && args(userId, secretId)")
	public void accessControlBeforeReadingSecret(JoinPoint joinPoint, String userId, UUID secretId) throws IOException {
		System.out.printf("Access control prior to reading secret %s\n", secretId );
		Boolean exists = stats.CheckForReadAccess(userId, secretId);

		if(!exists) {
			throw new NotAuthorizedException(userId+" doesnt have the access to read the message or secret doesn't exist");
		}
		
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.shareSecret(..)) && args(userId, secretId, targetUserId)")
	public void accessControlBeforeSharingSecret(JoinPoint joinPoint, String userId, UUID secretId, String targetUserId) throws IOException {
		System.out.printf("Access control prior to sharing secret %s\n", secretId );
		Boolean exists = stats.CheckForReadAccess(userId, secretId);

		if(!exists) {
			throw new NotAuthorizedException(userId+" doesnt have the access to share the message or secret doesn't exist");
		}
		
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..)) && args(userId, secretId, targetUserId)")
	public void accessControlBeforeUnSharingSecret(JoinPoint joinPoint, String userId, UUID secretId, String targetUserId) throws IOException {
		System.out.printf("Access control prior to unsharing secret %s\n", secretId );
		Boolean permission = stats.CheckForUnshareAccess(userId, secretId, targetUserId);
		if(!permission) {
			throw new NotAuthorizedException(userId+" doesnt have the access to unshare the message or secret doesn't exist");
		}
		
	}
}
