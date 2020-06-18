package com.raylee.tryjd.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.raylee.tryjd.model.dto.Goods;
import com.raylee.tryjd.model.dto.MenuDto;
import com.raylee.tryjd.model.enums.MenuEnum;
import com.raylee.tryjd.model.vo.QueryVo;
import com.raylee.tryjd.service.TryJDService;
import com.raylee.tryjd.utils.CacheUtil;
import com.raylee.tryjd.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
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
    @Value("${api.activities}")
    private String API_ACTIVITIES;
    @Value("${api.applyCounts}")
    private String API_APPLY_COUNTS;

    @Override
    public void Init() {
        try {
            Document doc = Jsoup.connect(API_ACTIVITIES).get();
            Elements line = doc.getElementById("selector").getElementsByClass("top").get(0)
                    .getElementsByClass("line");

            List<MenuDto> cids = line.get(0).getElementsByTag("dl").get(0).getElementsByTag("dd").get(0)
                    .getElementsByTag("a").stream().map(e -> {
                        MenuDto menu = new MenuDto();
                        menu.setKey(MenuEnum.CIDS);
                        menu.setName(e.text());
                        menu.setValue(e.attr("cids"));
                        return menu;
                    }).collect(Collectors.toList());

            List<MenuDto> types = line.get(1).getElementsByTag("dl").get(0).getElementsByTag("dd").get(0)
                    .getElementsByTag("a").stream().map(e -> {
                        String href = e.attr("href");
                        MenuDto menu = new MenuDto();
                        menu.setKey(MenuEnum.ACTIVITY_TYPE);
                        menu.setName(e.text());
                        menu.setValue(href.substring(href.lastIndexOf("=")));
                        return menu;
                    }).collect(Collectors.toList());

            List<MenuDto> states = line.get(2).getElementsByTag("dl").get(0).getElementsByTag("dd")
                    .get(0).getElementsByTag("a").stream().map(e -> {
                        String href = e.attr("href");
                        MenuDto menu = new MenuDto();
                        menu.setKey(MenuEnum.ACTIVITY_STATE);
                        menu.setName(e.text());
                        menu.setValue(href.substring(href.lastIndexOf("=")));
                        return menu;
                    }).collect(Collectors.toList());

            MenuDto menu1 = new MenuDto();
            menu1.setName("请选择");
            menu1.setValue(null);
            menu1.setKey(MenuEnum.CIDS);
            cids.add(0, menu1);

            MenuDto menu2 = new MenuDto();
            menu2.setName("请选择");
            menu2.setValue(null);
            menu2.setKey(MenuEnum.CIDS);
            types.add(0, menu2);

            MenuDto menu3 = new MenuDto();
            menu3.setName("请选择");
            menu3.setValue(null);
            menu3.setKey(MenuEnum.CIDS);
            states.add(0, menu3);

            log.info("\n试用分类:{}\n试用类型:{}\n试用状态:{}", cids, types, states);

            CacheUtil.put("cids", JSON.toJSONString(cids));
            CacheUtil.put("types", JSON.toJSONString(types));
            CacheUtil.put("states", JSON.toJSONString(states));
            CacheUtil.put("goods", JSON.toJSONString(analyGoods(doc)));
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
    public List<MenuDto> getCIds() {
        return JSON.parseArray(CacheUtil.get("cids"), MenuDto.class);
    }

    @Override
    public List<MenuDto> getTypes() {
        return JSON.parseArray(CacheUtil.get("types"), MenuDto.class);
    }

    @Override
    public List<MenuDto> getStates() {
        return JSON.parseArray(CacheUtil.get("states"), MenuDto.class);
    }

    @Override
    public List<Goods> getGoods(QueryVo vo) {
        String url = API_ACTIVITIES;
        Integer page = 1;
        if (vo.getPage() != null) {
            page = vo.getPage();
        }
        url += "?page=" + page;
        if (vo.getCids() != null) {
            url += "&" + MenuEnum.CIDS.getValue() + "=" + vo.getCids();
        }
        if (vo.getType() != null) {
            url += "&" + MenuEnum.ACTIVITY_TYPE.getValue() + "=" + vo.getType();
        }
        if (vo.getStatus() != null) {
            url += "&" + MenuEnum.ACTIVITY_STATE.getValue() + "=" + vo.getStatus();
        }
        try {
            return analyGoods(Jsoup.connect(url).get());
        } catch (Exception e) {
            log.error("获取商品失败!url:{}", url);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<Goods> analyGoods(Document doc) {
        List<Goods> goods = new LinkedList<>();
        Element good_list = doc.getElementById("goods-list");
        Elements items = good_list.getElementsByClass("item");
        items.forEach(e -> {
            Goods good = new Goods();
            good.setActivityId(e.attr("activity_id"));
            good.setSkuId(e.attr("sku_id"));
            good.setStartTime(new Date(Long.parseLong(e.attr("start_time"))));
            good.setEndTime(new Date(Long.parseLong(e.attr("end_time"))));
            good.setSysTime(new Date(Long.parseLong(e.attr("sys_time"))));
            good.setImage(e.getElementsByClass("p-img").get(0).getElementsByTag("img").get(0).attr("src"));
            good.setTitle(e.getElementsByClass("p-name").get(0).text());
            Elements detail = e.getElementsByClass("p-detail").get(0).getElementsByClass("t1").get(0).getElementsByTag("b");
            good.setTotal(detail.get(0).text());
            goods.add(good);
        });
        return getApplyCount(goods);
    }

    private List<Goods> getApplyCount(List<Goods> goods) {
        if (goods.isEmpty()) {
            return goods;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", API_ACTIVITIES);
        Map<String, String> params = new HashMap<>();
        params.put("activityIds", goods.stream().map(Goods::getActivityId).collect(Collectors.joining(",")));
        try {
            String result = OkHttpUtil.get(API_APPLY_COUNTS, headers, params);
            JSONArray arr = JSON.parseArray(result);
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                goods.stream().filter(e -> e.getActivityId().equals(obj.getString("activity_id"))).findFirst().get().setApply(obj.getString("count"));
            }
        } catch (Exception e) {
            log.error("请求商品申请量失败！");
            e.printStackTrace();
        }
        return goods;
    }

}
