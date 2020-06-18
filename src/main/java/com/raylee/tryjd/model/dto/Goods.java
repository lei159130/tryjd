package com.raylee.tryjd.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Goods {
    private String activityId;
    private String skuId;
    private String image;
    private String title;
    private String total;
    private String apply;
    private String price;
    private Date startTime;
    private Date endTime;
    private Date sysTime;
}
