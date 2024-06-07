package com.example.floatwindowdemo.sdk;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.atomic.AtomicReference;

public class DemoServiceRouter {

    static void handleConnect(@NonNull Message msg, @NonNull DemoService demoService) {
        Messenger clientMessenger = msg.replyTo;

        DemoExceptionHandler.handle(() -> {
            Bundle connectInfo = msg.getData();
            DemoClientInfo clientInfo = (DemoClientInfo) connectInfo.getSerializable(DemoConst.Key.CLIENT_INFO);

            DemoAssertHelper.assertNotNull(clientMessenger, DemoConst.ErrorMsg.NO_CLIENT_MESSENGER);
            DemoAssertHelper.assertNotNull(clientInfo, DemoConst.ErrorMsg.NO_CLIENT_INFO);

            Log.d(DemoConst.TAG, "server <=== client msg");
            Log.d(DemoConst.TAG, "双向通讯建立");
            demoService.registerClient(clientInfo, clientMessenger);

        }, new DemoExceptionHandler.HandleCallback() {
            @Override
            public void onSuccess() {
                DemoExceptionHandler.safeReplyMessage(clientMessenger,
                        DemoConst.ServiceReplyStatus.STATUS_CONNECTION_SUCCESS,
                        "connection success",
                        null);
            }

            @Override
            public void onFail(Exception error) {
                DemoExceptionHandler.safeReplyMessage(clientMessenger,
                        DemoConst.ServiceReplyStatus.STATUS_CONNECTION_FAIL,
                        error.getMessage(),
                        null);
            }
        });
    }

    static void handleDisconnect(@NonNull Message msg, @NonNull DemoService demoService) {
        Log.d(DemoConst.TAG, "ACTION_DISCONNECT");

        // 断连时不检查clientMessenger
        Messenger clientMessenger = msg.replyTo;

        DemoExceptionHandler.handle(() -> {
            demoService.disconnectClient(msg.getData());
        }, new DemoExceptionHandler.HandleCallback() {
            @Override
            public void onSuccess() {
                DemoExceptionHandler.safeReplyMessage(clientMessenger,
                        DemoConst.ServiceReplyStatus.STATUS_DISCONNECT_SUCCESS,
                        "Disconnect success",
                        null);
            }

            @Override
            public void onFail(Exception error) {
                DemoExceptionHandler.safeReplyMessage(clientMessenger,
                        DemoConst.ServiceReplyStatus.STATUS_DISCONNECT_FAIL,
                        error.getMessage(),
                        null);
            }
        });
    }

    static void handleAddWindow(@NonNull Message msg, @NonNull DemoService demoService) {
        Log.d(DemoConst.TAG, "ACTION_ADD_WINDOW");

        AtomicReference<Messenger> clientMessenger = new AtomicReference<>(msg.replyTo);

        DemoExceptionHandler.handle(() -> {
            ClientRecord clientRecord = demoService.checkClient(msg.getData());
            DemoAssertHelper.assertNotNull(clientRecord, DemoConst.ErrorMsg.CLIENT_NOT_REGISTER);

            clientMessenger.set(clientRecord.clientMessenger);
            DemoAssertHelper.assertNotNull(clientMessenger.get(), DemoConst.ErrorMsg.NO_CLIENT_MESSENGER);

            // 客户端校验通过，继续执行
            Bundle params = msg.getData();
            DemoOpenParams openParams = (DemoOpenParams) params.getSerializable(DemoConst.Key.OPEN_PARAMS);
            Class windowClass = openParams.windowClass;
            demoService.addFloatWindow(clientRecord, windowClass);
        }, new DemoExceptionHandler.HandleCallback() {
            @Override
            public void onSuccess() {
                DemoExceptionHandler.safeReplyMessage(clientMessenger.get(),
                        DemoConst.ServiceReplyStatus.STATUS_ADD_WINDOW_SUCCESS,
                        "Add window success",
                        null);
            }

            @Override
            public void onFail(Exception error) {
                DemoExceptionHandler.safeReplyMessage(clientMessenger.get(),
                        DemoConst.ServiceReplyStatus.STATUS_ADD_WINDOW_FAIL,
                        error.getMessage(),
                        null);
            }
        });
    }

    static void handleRemoveWindow(@NonNull Message msg, @NonNull DemoService demoService) {
        Log.d(DemoConst.TAG, "ACTION_REMOVE_WINDOW");
        AtomicReference<Messenger> clientMessenger = new AtomicReference<>();

        DemoExceptionHandler.handle(() -> {
            ClientRecord clientRecord = demoService.checkClient(msg.getData());
            DemoAssertHelper.assertNotNull(clientRecord, DemoConst.ErrorMsg.CLIENT_NOT_REGISTER);

            clientMessenger.set(clientRecord.clientMessenger);
            DemoAssertHelper.assertNotNull(clientMessenger.get(), DemoConst.ErrorMsg.NO_CLIENT_MESSENGER);

            demoService.removeFloatWindow();
        }, new DemoExceptionHandler.HandleCallback() {
            @Override
            public void onSuccess() {
                DemoExceptionHandler.safeReplyMessage(clientMessenger.get(),
                        DemoConst.ServiceReplyStatus.STATUS_REMOVE_WINDOW_SUCCESS,
                        "Remove window success",
                        null);
            }

            @Override
            public void onFail(Exception error) {
                DemoExceptionHandler.safeReplyMessage(clientMessenger.get(),
                        DemoConst.ServiceReplyStatus.STATUS_REMOVE_WINDOW_FAIL,
                        error.getMessage(),
                        null);
            }
        });
    }
}
