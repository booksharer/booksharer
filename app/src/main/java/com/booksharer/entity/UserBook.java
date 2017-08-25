package com.booksharer.entity;

import java.io.Serializable;

public class UserBook  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String bookInfoId;
    private Integer state;
    private Integer count;
    private String picture_1;
    private String picture_2;
    private String picture_3;
    private String picture_4;
    private String picture_5;
    private String picture_6;


    public String getUserId() {
        return userId;
   }

    public void setUserId(String userId) {
        this.userId = userId;
   }

    public String getBookInfoId() {
        return bookInfoId;
   }

    public void setBookInfoId(String bookInfoId) {
        this.bookInfoId = bookInfoId;
   }

    public Integer getState() {
        return state;
   }

    public void setState(Integer state) {
        this.state = state;
   }

    public Integer getCount() {
        return count;
   }

    public void setCount(Integer count) {
        this.count = count;
   }

    public String getPicture_1() {
        return picture_1;
   }

    public void setPicture_1(String picture_1) {
        this.picture_1 = picture_1;
   }

    public String getPicture_2() {
        return picture_2;
   }

    public void setPicture_2(String picture_2) {
        this.picture_2 = picture_2;
   }

    public String getPicture_3() {
        return picture_3;
   }

    public void setPicture_3(String picture_3) {
        this.picture_3 = picture_3;
   }

    public String getPicture_4() {
        return picture_4;
   }

    public void setPicture_4(String picture_4) {
        this.picture_4 = picture_4;
   }

    public String getPicture_5() {
        return picture_5;
   }

    public void setPicture_5(String picture_5) {
        this.picture_5 = picture_5;
   }

    public String getPicture_6() {
        return picture_6;
   }

    public void setPicture_6(String picture_6) {
        this.picture_6 = picture_6;
   }

    @Override
    public String toString() {
        return "UserBook{" +
                "userId=" + userId +
                ", bookInfoId=" + bookInfoId +
                ", state=" + state +
                ", count=" + count +
                ", picture_1='" + picture_1 + '\'' +
                ", picture_2='" + picture_2 + '\'' +
                ", picture_3='" + picture_3 + '\'' +
                ", picture_4='" + picture_4 + '\'' +
                ", picture_5='" + picture_5 + '\'' +
                ", picture_6='" + picture_6 + '\'' +
                '}';
    }
}

/*List columns as follows:
"user_id", "book_info_id", "state", "count", "picture_1", "picture_2", "picture_3", 
"picture_4", "picture_5", "picture_6"
*/