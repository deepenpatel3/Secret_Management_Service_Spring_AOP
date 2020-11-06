package edu.sjsu.cmpe275.aop.aspect;

import java.io.IOException;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.SecretStatsImpl;

@Aspect
//@Order(0)
@Order(3)
public class StatsAspect {
	@Autowired SecretStatsImpl stats;
	
	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..)) && args(userId, secretContent)", returning = "result" )
	public void RegisteringAfterCreatingSecret(JoinPoint joinPoint, Object result, String userId, String secretContent) throws IOException {

		stats.addSecretToConnection(userId, (UUID)result,secretContent);
		System.out.println("---------------------------------------------------------------------");
	}
	
	@AfterReturning("execution(public String edu.sjsu.cmpe275.aop.SecretService.readSecret(..)) && args(userId, secretId)" )
	public void UpdatingStatsnAfterReading(JoinPoint joinPoint, String userId, UUID secretId) throws IOException {

		stats.UpdateReadStats(userId, secretId);
		System.out.println("---------------------------------------------------------------------");
	}
	
	@AfterReturning("execution(public * edu.sjsu.cmpe275.aop.SecretService.shareSecret(..)) && args(userId, secretId, targetUserId)" )
	public void UpdatingSecretChainAfterSharing(JoinPoint joinPoint, String userId, UUID secretId, String targetUserId) throws IOException {

		stats.UpdateConnectionAfterSharing(userId, secretId, targetUserId);
		System.out.println("---------------------------------------------------------------------");
	}
	
	@AfterReturning("execution(public * edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..)) && args(userId, secretId, targetUserId)" )
	public void UpdatingSecretChainAfterUnsharing(JoinPoint joinPoint, String userId, UUID secretId, String targetUserId) throws IOException {
		
		stats.UpdateConnectionAfterUnsharing(userId, secretId, targetUserId);
		System.out.println("---------------------------------------------------------------------");
	}
}
