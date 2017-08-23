package com.booksharer.util;


import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                try {
//                    URL url = new URL(address);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    connection.setDoInput(true);
//                    connection.setDoOutput(true);
//                    InputStream in = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    if (listener != null) {
//                        // 回调onFinish()方法
//                        listener.onFinish(response.toString());
//                    }
//                } catch (Exception e) {
//                    if (listener != null) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } finally {
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                }
//            }
//        }).start();
//    }

    public static void sendOkHttpRequest(final String address, final Callback callback) {
        Log.d("test",address);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendOkHttpPost(final String address, HashMap<String, String> params,  final Callback callback) {
        Log.d("test",address);
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        Iterator i = params.entrySet().iterator();

        while (i.hasNext()) {
            Map.Entry entry = (java.util.Map.Entry) i.next();
            builder.add(entry.getKey().toString(), entry.getValue().toString());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpPostMultipart(final String address, HashMap<String, String> params, String pic_key, List<File> files, final Callback callback) {
        Log.d("test",address);
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (params != null){
            for (String key : params.keySet()) {
                multipartBodyBuilder.addFormDataPart(key, params.get(key));
            }
        }
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (files != null){
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }

        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
