package com.lin.httplib.base;

import com.lin.httplib.RequestManager;
import com.lin.httplib.itf.RequestObject;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRequestObject implements RequestObject, Runnable {

    protected final Map<String, Object> para = new HashMap<>();
    protected String url;
    protected RequestManager.Method method;

    protected BaseRequestObject(String url, RequestManager.Method method) {
        this.url = url;
        this.method = method;
    }

    @Override
    public void addParameter(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            para.putAll(map);
        }
    }

    @Override
    public void run() {

    }
}
