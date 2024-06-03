package com.example.floatwindowdemo.sdk;

import java.io.Serializable;

public class DemoOpenParams implements Serializable {

    public String name;
    public Class<? extends BaseFloatWindow> windowClass;
}
