package com.booksharer.entity;

import java.io.Serializable;
import java.util.Date;

public class Appointment implements Serializable{
	
	private long bookId;// 图书ID
    private long studentId;// 学号
    private Date appointTime;// 预约时间
    private Book book;
	public long getBookId() {
		return bookId;
	}
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
	public long getStudentId() {
		return studentId;
	}
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	public Date getAppointTime() {
		return appointTime;
	}
	public void setAppointTime(Date appointTime) {
		this.appointTime = appointTime;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	@Override
	public String toString() {
		return "Appointment [bookId=" + bookId + ", studentId=" + studentId
				+ ", appointTime=" + appointTime + "]";
	}
    
}
