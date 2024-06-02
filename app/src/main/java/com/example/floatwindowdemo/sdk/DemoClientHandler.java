package com.example.floatwindowdemo.sdk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

public class DemoClientHandler extends Handler {

    WeakReference<DemoClient> clientRef;

    public DemoClientHandler(@NonNull Looper looper, DemoClient client) {
        super(looper);
        this.clientRef = new WeakReference<>(client);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        DemoClient client = clientRef.get();
        if (client == null) {
            return;
        }
        if (msg.what == DemoConst.ACTION_CONNECT_ESTABLISHED) {
            DemoClientHelper.doLog(client, "server ===> client msg");
            DemoClientHelper.doLog(client, "双向通讯建立");
        } else if (msg.what == DemoConst.Callback.ERROR) {
            Bundle errorData = msg.getData();
            if (errorData == null) {
                return;
            }
            String errorMsg = errorData.getString(DemoConst.Callback.ERROR_MSG);
            if (!TextUtils.isEmpty(errorMsg)) {
                DemoClientHelper.doLog(client, errorMsg);
            }
        }
    }
}