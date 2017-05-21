package com.booksharer.entity;

import java.io.Serializable;

public class UserBookCommunity  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userId;
    private Integer bookCommunityId;
    private String joinDate;


    public Integer getUserId() {
        return userId;
   }

    public void setUserId(Integer userId) {
        this.userId = userId;
   }

    public Integer getBookCommunityId() {
        return bookCommunityId;
   }

    public void setBookCommunityId(Integer bookCommunityId) {
        this.bookCommunityId = bookCommunityId;
   }

    public String getJoinDate() {
        return joinDate;
   }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
   }

    @Override
    public String toString() {
        return "UserBookCommunity{" +
                "userId=" + userId +
                ", bookCommunityId=" + bookCommunityId +
                ", joinDate='" + joinDate + '\'' +
                '}';
    }
}

/*List columns as follows:
"user_id", "book_community_id", "join_date"
*/