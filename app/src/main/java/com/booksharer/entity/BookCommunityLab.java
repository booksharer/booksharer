package com.booksharer.entity;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class BookCommunityLab {

    private static BookCommunityLab sBookCommunityLab;
    private List<BookCommunity> mBookCommunities;

    @Override
    public String toString() {
        String str = "";
        for (int j = 0; j < mBookCommunities.size(); j++) {
            str += "/" + mBookCommunities.get(j);

        }


        return "BookCommunityLab{" +
                "mBookCommunities=" + str +
                '}';
    }

    public static BookCommunityLab get(String policyArray) throws JSONException {
        if (sBookCommunityLab == null) {
            sBookCommunityLab = new BookCommunityLab(policyArray);
        }
        return sBookCommunityLab;
    }

    public static BookCommunityLab get(Context context) {
        if (sBookCommunityLab == null) {
            sBookCommunityLab = new BookCommunityLab(context);
        }
        return sBookCommunityLab;
    }

    private BookCommunityLab(Context context){
        mBookCommunities = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            BookCommunity bookCommunity = new BookCommunity ();
            bookCommunity.setId( i);
            bookCommunity.setCommunityName("Crime #" + i);
            bookCommunity.setCommunityPeopleNum(i*5+2);
            bookCommunity.setCommunityLogo("url");
            mBookCommunities.add(bookCommunity);
        }    }
    private BookCommunityLab(String jsonData) throws JSONException {
        mBookCommunities = new ArrayList<>();
        Gson gson = new Gson();
        mBookCommunities = gson.fromJson(jsonData, new TypeToken<List<BookCommunity>>(){}.getType());
    }

    public List<BookCommunity> getBookCommunities() {
        return mBookCommunities;
    }

    public void setBookCommunities(List<BookCommunity> bookCommunities) {
        mBookCommunities = bookCommunities;
    }

    public static void dismiss() {
        sBookCommunityLab = null;
    }

}
