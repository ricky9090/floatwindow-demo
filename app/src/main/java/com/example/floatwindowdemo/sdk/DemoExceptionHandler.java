package com.example.floatwindowdemo.sdk;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class DemoExceptionHandler {

    public static void handle(HandleRunnable handleRunnable, HandleCallback handleCallback) {
        Exception error = null;

        try {
            handleRunnable.run();
        } catch (Exception e) {
            error = e;
        }

        if (handleCallback == null) {
            return;
        }

        if (error == null) {
            handleCallback.onSuccess();
        } else {
            handleCallback.onFail(error);
        }
    }

    public static void safeReplyMessage(Messenger clientMessenger, int status, String message, Bundle data) {
        try {
            Message replyMsg = Message.obtain();
            replyMsg.what = DemoConst.ACTION_SERVICE_REPLY;

            Bundle dataWrapper = new Bundle();
            dataWrapper.putInt(DemoConst.ServiceReplyKey.KEY_STATUS, status);
            dataWrapper.putString(DemoConst.ServiceReplyKey.KEY_MESSAGE, message);
            dataWrapper.putBundle(DemoConst.ServiceReplyKey.KEY_DATA, data);
            replyMsg.setData(dataWrapper);

            clientMessenger.send(replyMsg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public interface HandleRunnable {
        void run() throws Exception;
    }

    public interface HandleCallback {
        void onSuccess();
        void onFail(Exception error);
    }
}
