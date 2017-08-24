package com.booksharer.util;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by DELL on 2017/8/24.
 */
public class OkHttpUtil {
    public static void downloadImage(final String logo){
        String logo_url = "/sdcard/shuquan/"+logo;
        final File file = new File(logo_url);
        if (!file.exists()){
            Log.d("test","下载"+logo_url);
            HashMap<String, String> map = new HashMap<>();
            map.put("fileName",logo);
            MyApplication.setUrl_api("community/download",map);
            HttpUtil.sendOkHttpRequest(MyApplication.getUrl_api(),new okhttp3.Callback(){

                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream inputStream = response.body().byteStream();
                    //BitmapFactory.decodeStream(inputStream);
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int len;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.flush();
                    } catch (IOException e) {
                        Log.d("test", "IOException");
                        Log.d("test", logo);
                        e.printStackTrace();
                    }
                    Log.d("test", "文件下载成功");
                }
            });
        }
    }

}
