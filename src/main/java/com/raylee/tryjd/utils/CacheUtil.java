package com.raylee.tryjd.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class CacheUtil {
    private static class Singleton {
        private static Map<String, String> instance = new ConcurrentHashMap<>();
    }

    public static void put(String key, String value) {
        Singleton.instance.put(key, value);
    }

    public static String get(String key) {
        return Singleton.instance.get(key);
    }
}
