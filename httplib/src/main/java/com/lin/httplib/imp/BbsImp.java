package com.lin.httplib.imp;

import android.content.Intent;

import com.lin.httplib.RequestKernel;
import com.lin.httplib.base.BaseBss;

public class BbsImp extends BaseBss {

    @Override
    protected void handler(Token token) {
        RequestKernel.newCall(token);
    }
}
