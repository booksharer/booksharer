package com.booksharer.entity;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class BookInfoLab {
    private static BookInfoLab sBookInfoLab;
    private List<BookInfo> mBookResults;

    @Override
    public String toString() {
        String str = "";
        for (int j = 0; j < mBookResults.size(); j++) {
            str += "/" + mBookResults.get(j);

        }


        return "BookInfoLab{" +
                "mBookResults=" + str +
                '}';
    }

    public static BookInfoLab get(Context context) {
        if (sBookInfoLab == null) {
            sBookInfoLab = new BookInfoLab(context);
        }
        return sBookInfoLab;
    }

    private BookInfoLab(Context context){
        mBookResults = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            BookCommunity bookCommunity = new BookCommunity ();
//            bookCommunity.setId( i);
//            bookCommunity.setCommunityName("Crime #" + i);
//            bookCommunity.setCommunityPeopleNum(i*5+2);
//            bookCommunity.setCommunityLogo("url");
//            mBookCommunities.add(bookCommunity);
//        }
    }

    public void appendmBookResults(List<BookInfo> bookResults){
        mBookResults.addAll(bookResults) ;
    }

    public List<BookInfo> getmBookResults() {
        return mBookResults;
    }

    public void setmBookResults(List<BookInfo> bookInfos) {
        mBookResults = bookInfos;
    }

    public static void clear() {
        sBookInfoLab = null;
    }
}
