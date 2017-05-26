package com.booksharer.util;

import android.app.Application;
import android.content.Context;
import com.booksharer.entity.User;

/**
 * Created by DELL on 2017/5/23.
 */
public class MyApplication extends Application{
    private static Context context;
    private final static String url = "http://15269i5i69.51mypc.cn/";
    private static String url_api;
    private static User user;
    private static String position;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

    public static String getUrl_api() {
        return url_api;
    }

    public static void setUrl_api(String api) {
        url_api = url + api;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MyApplication.user = user;
    }

    public static String getPosition() {
        return position;
    }

    public static void setPosition(String position) {
        MyApplication.position = position;
    }
}
