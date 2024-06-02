package com.example.floatwindowdemo.sdk;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.Serializable;

public class DemoClientInfo implements Serializable {

    public String business;
    public String pageId;
    public final String token = "client_" + DemoClient.globalIndex.getAndIncrement() + "_" + System.currentTimeMillis();

    @Override
    public int hashCode() {
        return token.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof DemoClientInfo) {
            return TextUtils.equals(token, ((DemoClientInfo) obj).token);
        }
        return false;
    }
}
