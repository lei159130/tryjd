package com.raylee.tryjd.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring容器工具类
 * 
 * @title SpringUtil
 * @author 雷力
 * @date 2019年5月7日上午11:43:49
 *
 */
@Component
public class SpringUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String serviceName) {
		return applicationContext.getBean(serviceName);
	}

	public static Object getBean(String serviceName, Object... objs) {
		return applicationContext.getBean(serviceName, objs);
	}

	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	public static <T> T getBean(Class<T> clazz, Object... objs) {
		return applicationContext.getBean(clazz, objs);
	}
}
