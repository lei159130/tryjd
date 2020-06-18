package com.raylee.tryjd.model.vo;

import lombok.Data;

@Data
public class QueryVo {
    private String cids;
    private String type;
    private String status;
    private Integer page;
}
