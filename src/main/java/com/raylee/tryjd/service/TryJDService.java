package com.raylee.tryjd.service;

import java.util.List;
import java.util.Map;

/**
 * 京东试用Service
 * 
 * @title TryJDService
 * @author 雷力
 * @date 2019年11月5日上午9:51:51
 *
 */
public interface TryJDService {

	/**
	 * 设置cookies
	 * 
	 * @param cookies
	 */
	void setCookies(String cookies);

	/**
	 * 获取cookies
	 * 
	 * @return
	 */
	Map<String, String> getCookies();

	/**
	 * 获取商品分类
	 * 
	 * @return
	 */
	List<String> getCIds();

	/**
	 * 获取商品试用类型
	 * 
	 * @return
	 */
	List<String> getTypes();

	/**
	 * 获取商品试用状态
	 * 
	 * @return
	 */
	List<String> getStates();
}
