package com.booksharer.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.booksharer.entity.BookCommunity;
import com.booksharer.entity.BookCommunityLab;
import com.booksharer.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by DELL on 2016/8/18.
 */
public class Utility {
    /**
     * 解析查重结果
     *
     * @param response
     * @return
     */
    public static boolean handleFindRepeateResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.getBoolean("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleRegisterResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0)
                    return true;
                else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public static boolean handleLoginResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0)
                    return true;
                else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public static boolean handleFindNearCommunityResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("state") == 0) {
                    BookCommunityLab.get(jsonObject.getJSONObject("data").toString());
                    return true;
                } else
                    Toast.makeText(MyApplication.getContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
