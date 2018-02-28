package com.doc.bean;

/**
 * Created by Administrator on 2016/9/16.
 */
public enum FieldType {
    ARRAY("array"),
    OBJECT("object");

    private String value;

    FieldType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
