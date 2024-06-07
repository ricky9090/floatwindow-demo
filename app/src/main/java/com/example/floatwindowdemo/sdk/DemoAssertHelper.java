package com.example.floatwindowdemo.sdk;

public class DemoAssertHelper {

    public static void assertNotNull(Object obj, String errorMsg) throws Exception {
        if (obj == null) throw new NullPointerException(errorMsg);
    }
}
