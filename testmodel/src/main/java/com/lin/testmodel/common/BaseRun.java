package com.lin.testmodel.common;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseRun implements CodeManager.Runnable {

    protected AtomicBoolean isStart = new AtomicBoolean(false);
    protected CodeManager.Config config;
    protected BaseRun(CodeManager.Config config) {
        this.config = config;
    }

    @Override
    public boolean isStart() {
        return isStart.get();
    }

    public void setStart(boolean b){
        isStart.set(b);
    }




}
