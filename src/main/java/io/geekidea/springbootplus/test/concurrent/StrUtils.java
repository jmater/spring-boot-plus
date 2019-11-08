package io.geekidea.springbootplus.test.concurrent;

import java.util.UUID;

public class StrUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","") + String.valueOf(System.currentTimeMillis());
    }
}
