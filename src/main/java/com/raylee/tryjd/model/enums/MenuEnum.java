package com.raylee.tryjd.model.enums;

public enum MenuEnum {
    CIDS("cids"),
    ACTIVITY_TYPE("activityType"),
    ACTIVITY_STATE("activityState");
    private String value;

    MenuEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
