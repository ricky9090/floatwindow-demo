package com.example.floatwindowdemo.sdk;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(DemoConst.TAG, "onTerminate");
        onDestroyDemoService();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(DemoConst.TAG, "onLowMemory");
        onDestroyDemoService();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(DemoConst.TAG, "onTrimMemory");
        onDestroyDemoService();
    }

    private void onDestroyDemoService() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), DemoService.class);
        getApplicationContext().stopService(intent);
    }
}
