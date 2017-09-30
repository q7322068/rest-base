package com.onecoderspace.base.aop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

@Component
@Aspect
public class WebLogAspect {

	private static final Logger logger = LoggerFactory
			.getLogger(WebLogAspect.class);

	ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<Long>();

	@Pointcut("execution(public * com.onecoderspace.base.controller..*.*(..))")
	public void webLog() {
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		startTimeThreadLocal.set(System.currentTimeMillis());
	}

	@AfterReturning(pointcut = "webLog()")
	public void doAfterReturning(JoinPoint joinPoint) throws Throwable {
		long usedTime = System.currentTimeMillis() - startTimeThreadLocal.get();
		Object[] arr = joinPoint.getArgs();
		List<Object> list = Lists.newArrayList();
		for(Object obj : arr){
			if(obj == null){
				continue;
			}
			if(obj instanceof HttpServletRequest || obj instanceof HttpServletResponse){
				continue;
			}
			list.add(obj.toString());
		}
		String args = Joiner.on(",").join(list);
		String key = String.format("method=[%s.%s]",
				joinPoint.getSignature().getDeclaringType().getSimpleName(),
				joinPoint.getSignature().getName());
		if(usedTime > 100){
			logger.info(String.format("method=[%s],args=[%s] use time=%d ms",key, args, usedTime));
		} else if(logger.isDebugEnabled()){
				logger.debug(String.format("method=[%s],args=[%s] use time=%d ms",key, args, usedTime));
		}
		
	}
	
}