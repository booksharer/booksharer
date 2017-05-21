package com.booksharer.entity;

import java.io.Serializable;

public class BorrowingRecord  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userBorrowerId;
    private Integer userRenderId;
    private Integer bookId;
    private String borrowDate;
    private String appointBackDate;
    private String realBackDate;
    private Integer state;


    public Integer getUserBorrowerId() {
        return userBorrowerId;
   }

    public void setUserBorrowerId(Integer userBorrowerId) {
        this.userBorrowerId = userBorrowerId;
   }

    public Integer getUserRenderId() {
        return userRenderId;
   }

    public void setUserRenderId(Integer userRenderId) {
        this.userRenderId = userRenderId;
   }

    public Integer getBookId() {
        return bookId;
   }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
   }

    public String getBorrowDate() {
        return borrowDate;
   }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
   }

    public String getAppointBackDate() {
        return appointBackDate;
   }

    public void setAppointBackDate(String appointBackDate) {
        this.appointBackDate = appointBackDate;
   }

    public String getRealBackDate() {
        return realBackDate;
   }

    public void setRealBackDate(String realBackDate) {
        this.realBackDate = realBackDate;
   }

    public Integer getState() {
        return state;
   }

    public void setState(Integer state) {
        this.state = state;
   }

    @Override
    public String toString() {
        return "BorrowingRecord{" +
                "userBorrowerId=" + userBorrowerId +
                ", userRenderId=" + userRenderId +
                ", bookId=" + bookId +
                ", borrowDate='" + borrowDate + '\'' +
                ", appointBackDate='" + appointBackDate + '\'' +
                ", realBackDate='" + realBackDate + '\'' +
                ", state=" + state +
                '}';
    }
}

/*List columns as follows:
"user_borrower_id", "user_render_id", "book_id", "borrow_date", "appoint_back_date", "real_back_date", "state"
*/