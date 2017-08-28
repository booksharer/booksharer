package com.booksharer.entity;

import android.content.Context;

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
            str += "/" + mBookCommunities.get(j).toString();

        }


        return "BookCommunityLab{" +
                "mBookCommunities=" + str +
                '}';
    }

    public static BookCommunityLab get(Context context) {
        if (sBookCommunityLab == null) {
            sBookCommunityLab = new BookCommunityLab(context);
        }
        return sBookCommunityLab;
    }

    private BookCommunityLab(Context context){
        mBookCommunities = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            BookCommunity bookCommunity = new BookCommunity ();
//            bookCommunity.setId( i);
//            bookCommunity.setCommunityName("Crime #" + i);
//            bookCommunity.setCommunityPeopleNum(i*5+2);
//            bookCommunity.setCommunityLogo("url");
//            mBookCommunities.add(bookCommunity);
//        }
    }


    public void appendBookCommunities(List<BookCommunity> bookCommunities){
        mBookCommunities.addAll(bookCommunities) ;
    }

    public List<BookCommunity> getBookCommunities() {
        return mBookCommunities;
    }

    public void setBookCommunities(List<BookCommunity> bookCommunities) {
        mBookCommunities = bookCommunities;
    }

    public static void clear() {
        sBookCommunityLab = null;
    }

}
