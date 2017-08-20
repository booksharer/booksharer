package com.booksharer.util;

import android.app.Application;
import android.content.Context;
import com.booksharer.entity.User;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by DELL on 2017/5/23.
 */
public class MyApplication extends Application{
    private static Context context;
    private final static String url = "http://182.92.176.143:8888/bookshare/";
    private static String url_api;
    private static User user;
    private static String position;
    private static String area = "定位失败";


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

    public static void setUrl_api(String api, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append(api).append("?");

        Iterator i = params.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (java.util.Map.Entry) i.next();
            stringBuilder
                    .append(entry.getKey().toString())
                    .append("=")
                    .append(entry.getValue().toString())
                    .append("&");
        }
        url_api =  stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
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

    public static String getArea() {
        return area;
    }

    public static void setArea(String area) {
        MyApplication.area = area;
    }
}
