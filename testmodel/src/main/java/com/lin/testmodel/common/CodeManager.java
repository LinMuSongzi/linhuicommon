package com.lin.testmodel.common;

import android.graphics.Bitmap;

public class CodeManager {
    static CodeManager codeManager;

    static {
        codeManager = new CodeManager();
    }

    private CodeManager() {

    }

    public static Object getInstance() {
        return codeManager;
    }

    public static Runnable build(Config config) {
        return new RunImp(config);
    }

    public interface Runnable {
        boolean isStart();

        void start();

        void stop();
    }

    public interface Config {

        String getAudioVoice();
    }
}
