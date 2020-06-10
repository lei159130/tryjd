package com.raylee.tryjd.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.raylee.tryjd.service.TryJDService;
import com.raylee.tryjd.utils.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 京东试用service
 *
 * @author 雷力
 * @title TryJDServiceImpl
 * @date 2019年11月5日下午2:20:17
 */
@Slf4j
@Service
public class TryJDServiceImpl implements TryJDService {
    @Value("${api.activitys}")
    private String API_ACTIVITYS;

    @Override
    public void Init() {
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
                    .getElementsByTag("a").stream().map(e -> {
                        JSONObject json = new JSONObject();
                        String href = e.attr("href");
                        json.put("name", e.text());
                        json.put("value", href.substring(href.lastIndexOf("=")));
                        return json;
                    }).collect(Collectors.toList()));

            String states = JSON.toJSONString(line.get(2).getElementsByTag("dl").get(0).getElementsByTag("dd")
                    .get(0).getElementsByTag("a").stream().map(e -> {
                        JSONObject json = new JSONObject();
                        String href = e.attr("href");
                        json.put("name", e.text());
                        json.put("value", href.substring(href.lastIndexOf("=")));
                        return json;
                    }).collect(Collectors.toList()));

            log.info("\n试用分类:{}\n试用类型:{}\n试用状态:{}", cids, types, states);

            CacheUtil.put("cids", cids);
            CacheUtil.put("types", types);
            CacheUtil.put("states", states);
        } catch (IOException e) {
            log.error("获取试用分类、试用类型、试用状态失败!");
            e.printStackTrace();
        }
    }

    @Override
    public void setCookies(String cookies) {
        CacheUtil.put("cookies", cookies);
    }

    @Override
    public Map<String, String> getCookies() {
        return JSON.parseObject(CacheUtil.get("cookies")).toJavaObject(new TypeReference<Map<String, String>>() {
            ;
        });
    }

    @Override
    public List<String> getCIds() {
        return JSON.parseArray(CacheUtil.get("cids"), String.class);
    }

    @Override
    public List<String> getTypes() {
        return JSON.parseArray(CacheUtil.get("types"), String.class);
    }

    @Override
    public List<String> getStates() {
        return JSON.parseArray(CacheUtil.get("states"), String.class);
    }

}
