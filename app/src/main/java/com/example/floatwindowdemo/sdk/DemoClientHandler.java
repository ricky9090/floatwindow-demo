package com.example.floatwindowdemo.sdk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

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
        if (msg.what == DemoConst.ACTION_SERVICE_REPLY) {
            handleServiceReply(msg, client);

        }
    }

    private void handleServiceReply(@NonNull Message msg, @NonNull DemoClient client) {
        Bundle dataWrapper = msg.getData();
        if (dataWrapper == null) {
            return;
        }

        try {
            int status = dataWrapper.getInt(DemoConst.ServiceReplyKey.KEY_STATUS);
            String message = dataWrapper.getString(DemoConst.ServiceReplyKey.KEY_MESSAGE);
            Bundle data = dataWrapper.getBundle(DemoConst.ServiceReplyKey.KEY_DATA);

            DemoResult result = new DemoResult();
            result.status = status;
            result.message = message;
            result.data = data;

            client.onReceiveMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}