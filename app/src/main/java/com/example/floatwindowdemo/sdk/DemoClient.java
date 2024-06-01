package com.example.floatwindowdemo.sdk;

import android.os.Bundle;
import android.text.TextUtils;

public class DemoClient {

    private final DemoClientFragment clientFragment;
    ClientCallback clientCallback;

    public DemoClient(DemoClientFragment fragment) {
        clientFragment = fragment;
    }

    public void setClientCallback(ClientCallback clientCallback) {
        this.clientCallback = clientCallback;
    }

    public void sendMessage(int msg, Bundle params) {
        clientFragment.sendMessage(msg, params);
    }

    void onReceiveMessage(String msg) {
        if (clientCallback != null && !TextUtils.isEmpty(msg)) {
            clientCallback.onReceive(msg);
        }
    }

    public interface ClientCallback {
        void onReceive(String message);
    }
}
