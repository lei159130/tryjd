package com.raylee.tryjd.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Component
public class CacheManager {

	private static String API_ACTIVITYS;

	static {
		try {
			Document doc = Jsoup.connect(API_ACTIVITYS).get();
			Element selector = doc.getElementById("selector");
			Elements dds = selector.getElementsByClass("dd");

			String cids = JSON
					.toJSONString(dds.get(0).getElementsByTag("a").stream().map(new Function<Element, JSONObject>() {
						@Override
						public JSONObject apply(Element e) {
							JSONObject json = new JSONObject();
							json.put("name", e.text());
							json.put("value", e.attr("cids"));
							return json;
						}
					}).collect(Collectors.toList()));

			String types = JSON
					.toJSONString(dds.get(1).getElementsByTag("a").stream().map(new Function<Element, JSONObject>() {
						@Override
						public JSONObject apply(Element e) {
							JSONObject json = new JSONObject();
							String href = e.attr("href");
							json.put("name", e.text());
							json.put("value", href.substring(href.lastIndexOf("=")));
							return json;
						}
					}).collect(Collectors.toList()));

			String states = JSON
					.toJSONString(dds.get(2).getElementsByTag("a").stream().map(new Function<Element, JSONObject>() {
						@Override
						public JSONObject apply(Element e) {
							JSONObject json = new JSONObject();
							String href = e.attr("href");
							json.put("name", e.text());
							json.put("value", href.substring(href.lastIndexOf("=")));
							return json;
						}
					}).collect(Collectors.toList()));

			Singleton.instance.put("cids", cids);
			Singleton.instance.put("types", types);
			Singleton.instance.put("states", states);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class Singleton {
		private static Map<String, String> instance = new ConcurrentHashMap<>();
	}

	public void put(String key, String value) {
		Singleton.instance.put(key, value);
	}

	public String get(String key) {
		return Singleton.instance.get(key);
	}

	@Value("${api.activitys}")
	public void setAPI_ACTIVITYS(String API_ACTIVITYS) {
		CacheManager.API_ACTIVITYS = API_ACTIVITYS;
	}
}
