package com.lin.httplib.itf;

import android.app.Service;

import java.io.Serializable;
import java.util.Map;

public interface RequestObject extends Serializable {
    void addParameter(Map<String,Object> o);
}
