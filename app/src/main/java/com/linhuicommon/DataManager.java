package com.linhuicommon;

import com.lin.httplib.RequestManager;
import com.lin.httplib.itf.RequestObject;
import com.linhuicommon.common.Urls;

class DataManager {

    public static void getSimpleConfig() {
        RequestManager requestManger = RequestManager.getInstance();
        RequestObject requestObject = RequestManager.buildRequestObject(Urls.getSimpleConfig(),RequestManager.Method.Get);
//        requestObject.addParameter(null);
        requestManger.handlRequest(requestObject);
    }

    public static void upLpadSimpleConfig(String filePath) {
        RequestManager requestManger = RequestManager.getInstance();
        RequestObject requestObject = RequestManager.buildFileRequestObject(Urls.getSimpleConfig(),RequestManager.Method.Post,filePath);
//        requestObject.addParameter(null);
        requestManger.handlRequest(requestObject);
    }
}
