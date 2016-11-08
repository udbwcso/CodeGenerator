package com.result;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */
public class ResultMap {
    private String id;
    private ResultHandler resultHandler;
    private List<ResultField> fieldList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResultHandler getResultHandler() {
        return resultHandler;
    }

    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public List<ResultField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<ResultField> fieldList) {
        this.fieldList = fieldList;
    }
}
