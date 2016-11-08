package com.result;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2016/11/4.
 */
public interface ResultHandler<T> {

    T prepare(T bean);

    Map<String, Object> toMap(T bean, JSONObject template);

}
