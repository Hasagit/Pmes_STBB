package com.ruiduoyi;

import android.app.Application;
import android.content.Context;

import com.ruiduoyi.utils.CrashHandler;

/**
 * Created by DengJf on 2017/7/3.
 */

public class RdyApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());

    }


}
