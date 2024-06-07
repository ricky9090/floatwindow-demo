package com.example.floatwindowdemo.sdk;

public class DemoConst {

    public static final String TAG = "Demo-0531";

    public static final int ACTION_CONNECT = 900;
    public static final int ACTION_SERVICE_REPLY = 901;

    public static final int ACTION_ADD_WINDOW = 1000;
    public static final int ACTION_REMOVE_WINDOW = 1001;

    public static final int ACTION_AUTO_PLAY = 1002;

    public static final int ACTION_DISCONNECT = 1100;

    public interface Key {
        String WINDOW_CLASS = "key_window_class";
        String CLIENT_INFO = "key_client_info";

        String OPEN_PARAMS = "key_open_params";
    }

    public interface ServiceReplyKey {
        String KEY_STATUS = "key_callback_status";
        String KEY_MESSAGE = "key_callback_message";
        String KEY_DATA = "key_callback_data";
    }

    public interface ServiceReplyStatus {
        int STATUS_CONNECTION_SUCCESS = 200;
        int STATUS_CONNECTION_FAIL = 201;
        int STATUS_DISCONNECT_SUCCESS = 202;
        int STATUS_DISCONNECT_FAIL = 203;

        int STATUS_ADD_WINDOW_SUCCESS = 210;
        int STATUS_ADD_WINDOW_FAIL = 211;

        int STATUS_REMOVE_WINDOW_SUCCESS = 220;
        int STATUS_REMOVE_WINDOW_FAIL = 221;
    }

    public interface ErrorMsg {
        String NO_CLIENT_INFO = "no client info";
        String NO_CLIENT_MESSENGER = "no client messenger";
        String CLIENT_NOT_REGISTER = "client not register";
        String INVALID_PARAMS = "invalid params";
    }

    public interface Callback {
        String ERROR_MSG = "key_error_msg";
        int ERROR = 500;
    }


}
