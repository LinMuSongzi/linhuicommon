package com.lin.httplib.imp.para;

import com.lin.httplib.RequestManager;
import com.lin.httplib.base.BaseRequestObject;

public class FileRequestObject extends BaseRequestObject {

    protected String filePath;

    public FileRequestObject(String url, RequestManager.Method method,String filePath) {
        super(url, method);
        this.filePath = filePath;
    }
}
