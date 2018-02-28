package com.doc.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ResultBean {
    private String id;
    private List<Field> fieldList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }
}
