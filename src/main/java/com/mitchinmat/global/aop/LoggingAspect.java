package com.mitchinmat.global.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.mitchinmat.global.error.exception.MitchinmatException;
import com.mitchinmat.global.error.exception.NonLoggableException;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

	@Pointcut("within(@com.mitchinmat.global.aop.LogExecution *)")
	public void beanAnnotatedWithLogExecution() {
	}

	@Around("beanAnnotatedWithLogExecution()")
	public Object logExceptionsAndInputs(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getSignature().getDeclaringTypeName();
		try {
			Object result = joinPoint.proceed();
			log.info(getSuccessLogMessage(className, methodName, args));
			return result;
		} catch (MitchinmatException ex) {
			throw ex;
		} catch (NonLoggableException ex) {
			log.info(getErrorLogMessage(className, methodName, args, ex.getMessage()));
			throw ex;
		} catch (Exception ex) {
			throw ex;
		}
	}

	private String getSuccessLogMessage(String className, String methodName, Object[] args) {
		return String.format("%s.%s() with arguments:%s Successfully executed",
			className, methodName, Arrays.toString(args));
	}

	private String getErrorLogMessage(String className, String methodName, Object[] args, String errorMessage) {
		return String.format("Exception in %s. %s() called with arguments:%s with message:%s",
			className, methodName, Arrays.toString(args), errorMessage);
	}
}
