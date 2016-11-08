package com.result;

/**
 * Created by Administrator on 2016/11/4.
 */
public abstract class BaseResultHandler<T> implements ResultHandler<T>{

    protected ResultMap resultMap;

    @Override
    public T prepare(T bean) {
        return bean;
    }

//    @Override
//    public Map<String, Object> toMap(T bean, JSONObject template) {
//        return null;
//    }
}
