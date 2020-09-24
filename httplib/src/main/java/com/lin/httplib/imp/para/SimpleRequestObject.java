package com.lin.httplib.imp.para;

import com.lin.httplib.RequestManager;
import com.lin.httplib.base.BaseRequestObject;

public class SimpleRequestObject extends BaseRequestObject {


    public SimpleRequestObject(String url, RequestManager.Method method) {
        super(url, method);
    }
}
