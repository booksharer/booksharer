package com.booksharer.util;

import android.util.Log;
import android.widget.Toast;

import com.booksharer.entity.Book;
import com.booksharer.entity.BookInfo;
import com.booksharer.entity.UserBook;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by DELL on 2017/8/24.
 */
public class OkHttpUtil {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final String TAG = "OkHttpUtil";

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

    public static void sendMultipartBook(String path, final BookInfo mBook, final String mBookNum){
        final File file = new File(path);
        MyApplication.setUrl_api("book/add");
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "bookPic")
                .addFormDataPart("image", "bookPic.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, file))
                .addFormDataPart("bookName",mBook.getBookName())
                .addFormDataPart("author",mBook.getAuthor())
                .addFormDataPart("publisher",mBook.getPublisher())
                .addFormDataPart("publishDate",mBook.getPublishDate())
                .addFormDataPart("price",mBook.getPrice())
                .addFormDataPart("isbn",mBook.getIsbn())
                .addFormDataPart("contentIntro",mBook.getContentIntro())
                .addFormDataPart("authorIntro",mBook.getAuthorIntro())
                .addFormDataPart("tags",mBook.getTags())
                .addFormDataPart("zhuangZhen",mBook.getZhuangZhen())
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url(MyApplication.getUrl_api())
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, response.body().string());
                try {
//                    Log.d(TAG,response.toString());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getInt("state") == 0) {
                        JSONObject data = new JSONObject(jsonObject.getString("data"));
                        mBook.setId(data.getString("id"));
                        UserBook ub = new UserBook();
                        ub.setUserId(MyApplication.getUser().getId());
                        ub.setBookInfoId(mBook.getId());
                        MyApplication.getUserBooks().add(ub);
                        Log.d(TAG,ub.toString());
                        Log.d(TAG,MyApplication.getUserBooks().toString());
                        if(ub != null){
                            ub.setCount(mBookNum);
                            OkHttpUtil.sendMultipartUserBook(ub);
                        }
                    }else{
                        Log.d(TAG,jsonObject.getString("desc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void sendMultipartUserBook(UserBook ub) {
        MyApplication.setUrl_api("userBook/add");
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody requestbody = new FormBody.Builder()
                .add("userId", ub.getUserId())
                .add("bookInfoId", ub.getBookInfoId())
                .add("count", String.valueOf(ub.getCount()))
                .build();
        Request request = new Request.Builder()
                .url(MyApplication.getUrl_api())
                .post(requestbody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().string());
            }

        });

    }
}