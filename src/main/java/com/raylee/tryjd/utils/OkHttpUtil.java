package com.raylee.tryjd.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class OkHttpUtil {
    private static class Singleton {
        private static OkHttpClient client = new OkHttpClient();
    }

    public static String get(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new Exception("链接不能为空！");
        }
        Request.Builder reqBuilder = new Request.Builder();

        if (headers != null) {
            headers.entrySet().forEach(e -> {
                reqBuilder.addHeader(e.getKey(), e.getValue());
            });
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            params.entrySet().forEach(e -> {
                urlBuilder.addQueryParameter(e.getKey(), e.getValue());
            });
        }

        try (Response response = Singleton.client.newCall(reqBuilder.url(urlBuilder.build()).build()).execute()) {
            String result = response.body().string();
            if (StringUtils.isEmpty(result)) {
                throw new Exception("请求异常，返回结果为空！");
            }
            return result;
        }
    }
}
