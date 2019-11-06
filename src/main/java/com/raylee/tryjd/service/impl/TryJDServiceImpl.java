package com.raylee.tryjd.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.raylee.tryjd.config.ParamsCache;
import com.raylee.tryjd.service.TryJDService;

/**
 * 京东试用service
 * 
 * @title TryJDServiceImpl
 * @author 雷力
 * @date 2019年11月5日下午2:20:17
 *
 */
@Service
public class TryJDServiceImpl implements TryJDService {

	@Autowired
	ParamsCache paramsCache;

	@Override
	public void setCookies(String cookies) {
		paramsCache.put("cookies", cookies);
	}

	@Override
	public Map<String, String> getCookies() {
		return JSON.parseObject(paramsCache.get("cookies")).toJavaObject(new TypeReference<Map<String, String>>() {
			;
		});
	}

	@Override
	public List<String> getCIds() {
		return JSON.parseArray(paramsCache.get("cids"), String.class);
	}

	@Override
	public List<String> getTypes() {
		return JSON.parseArray(paramsCache.get("types"), String.class);
	}

	@Override
	public List<String> getStates() {
		return JSON.parseArray(paramsCache.get("states"), String.class);
	}

}
