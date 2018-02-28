package com.result;

/**
 * Created by Administrator on 2016/11/8.
 */
public enum FieldType {
    ARRAY("array"),
    OBJECT("object");

    private String code;

    FieldType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
