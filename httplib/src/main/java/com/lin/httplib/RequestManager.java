package com.lin.httplib;

import com.lin.httplib.imp.BbsImp;
import com.lin.httplib.itf.BusinessServiceSupport;
import com.lin.httplib.imp.para.FileRequestObject;
import com.lin.httplib.imp.para.SimpleRequestObject;
import com.lin.httplib.itf.RequestObject;

public class RequestManager {
    public static RequestManager getInstance() {
        return null;
    }

    public static RequestObject buildRequestObject(String getSimpleConfig, Method method) {
        return new SimpleRequestObject(getSimpleConfig,method);
    }

    public static RequestObject buildFileRequestObject(String getSimpleConfig, Method method,String f) {
        return new FileRequestObject(getSimpleConfig,method,f);
    }

    public static BusinessServiceSupport BuildBss() {
        return new BbsImp();
    }

    public void addRequest(Object tag, RequestObject buildRequestObject) {
    }

    public void handlRequest(RequestObject requestObject) {


    }

    public static enum  Method {
         Get,
        Post
    }
}
