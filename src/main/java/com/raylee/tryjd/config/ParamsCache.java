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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ParamsCache {

	private static String API_ACTIVITYS;

	private static class Singleton {
		private static Map<String, String> instance = new ConcurrentHashMap<>();

		static {
			try {
				Document doc = Jsoup.connect(API_ACTIVITYS).get();
				Elements line = doc.getElementById("selector").getElementsByClass("top").get(0)
						.getElementsByClass("line");

				String cids = JSON.toJSONString(line.get(0).getElementsByTag("dl").get(0).getElementsByTag("dd").get(0)
						.getElementsByTag("a").stream().map(new Function<Element, JSONObject>() {
							@Override
							public JSONObject apply(Element e) {
								JSONObject json = new JSONObject();
								json.put("name", e.text());
								json.put("value", e.attr("cids"));
								return json;
							}
						}).collect(Collectors.toList()));

				String types = JSON.toJSONString(line.get(1).getElementsByTag("dl").get(0).getElementsByTag("dd").get(0)
						.getElementsByTag("a").stream().map(new Function<Element, JSONObject>() {
							@Override
							public JSONObject apply(Element e) {
								JSONObject json = new JSONObject();
								String href = e.attr("href");
								json.put("name", e.text());
								json.put("value", href.substring(href.lastIndexOf("=")));
								return json;
							}
						}).collect(Collectors.toList()));

				String states = JSON.toJSONString(line.get(2).getElementsByTag("dl").get(0).getElementsByTag("dd")
						.get(0).getElementsByTag("a").stream().map(new Function<Element, JSONObject>() {
							@Override
							public JSONObject apply(Element e) {
								JSONObject json = new JSONObject();
								String href = e.attr("href");
								json.put("name", e.text());
								json.put("value", href.substring(href.lastIndexOf("=")));
								return json;
							}
						}).collect(Collectors.toList()));

				log.info("\n试用分类:{}\n试用类型:{}\n试用状态:{}", cids, types, states);

				instance.put("cids", cids);
				instance.put("types", types);
				instance.put("states", states);
			} catch (IOException e) {
				log.error("获取试用分类、试用类型、试用状态失败!");
				e.printStackTrace();
			}
		}
	}

	public void put(String key, String value) {
		Singleton.instance.put(key, value);
	}

	public String get(String key) {
		return Singleton.instance.get(key);
	}

	@Value("${api.activitys}")
	public void setAPI_ACTIVITYS(String API_ACTIVITYS) {
		ParamsCache.API_ACTIVITYS = API_ACTIVITYS;
	}

}
