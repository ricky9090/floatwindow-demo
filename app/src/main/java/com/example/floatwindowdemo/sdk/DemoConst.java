package com.example.floatwindowdemo.sdk;

public class DemoConst {

    public static final String TAG = "Demo-0531";

    public static final int ACTION_CONNECT = 900;
    public static final int ACTION_CONNECT_ESTABLISHED = 901;


    public static final int ACTION_ADD_WINDOW = 1000;
    public static final int ACTION_REMOVE_WINDOW = 1001;

    public static final int ACTION_AUTO_PLAY = 1002;

    public static final int ACTION_DISCONNECT = 1100;

    public interface Key {
        String WINDOW_CLASS = "key_window_class";
        String CLIENT_INFO = "key_client_info";

        String OPEN_PARAMS = "key_open_params";
    }

    public interface Callback {
        String ERROR_MSG = "key_error_msg";
        int ERROR = 500;
    }


}
