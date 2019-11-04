package com.raylee.tryjd.utils;

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 网络请求工具类
 * 
 * @Title: HttpRequestUtils
 * @Description:
 * @Auther: 雷力
 * @Date: 2018-10-25 17:16:55
 *
 */
@Slf4j
public class HttpRequestUtils {

	private static String cookies;

	public static void setCookies(String cookies) {
		HttpRequestUtils.cookies = cookies;
	}

	/**
	 * GET请求
	 * 
	 * @param url   链接
	 * @param param 参数
	 * @return
	 */
	public static String doGet(String url, JSONObject param) {

		CloseableHttpClient httpClient = HttpClientUtils.getCloseableHttpClient();
		String result = "";

		try {
			URIBuilder builder = new URIBuilder(url);

			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.getString(key));
				}
			}
			URI uri = builder.build();
			HttpGet httpGet = new HttpGet(uri);
			CloseableHttpResponse httpResponse = null;

			try {
				httpGet.setHeader("Cookie", cookies);
				httpGet.setConfig(HttpClientUtils.getRequestConfig());
				httpResponse = httpClient.execute(httpGet);
			} catch (Exception e) {
				log.error("请求超时【{}】", url);
				return doGet(url, param);
			}

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			} else {
				throw new RuntimeException("StatusCode:" + httpResponse.getStatusLine().getStatusCode() + ";请求错误!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
