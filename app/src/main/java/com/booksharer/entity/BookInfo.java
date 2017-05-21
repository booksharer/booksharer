package com.booksharer.entity;

import java.io.Serializable;

public class BookInfo  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String bookName;
    private String author;
    private String publisher;
    private String publishDate;
    private String price;
    private String isbn;
    private Integer pageNum;
    private String bookPic;


    public Integer getId() {
        return id;
   }

    public void setId(Integer id) {
        this.id = id;
   }

    public String getBookName() {
        return bookName;
   }

    public void setBookName(String bookName) {
        this.bookName = bookName;
   }

    public String getAuthor() {
        return author;
   }

    public void setAuthor(String author) {
        this.author = author;
   }

    public String getPublisher() {
        return publisher;
   }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
   }

    public String getPublishDate() {
        return publishDate;
   }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
   }

    public String getPrice() {
        return price;
   }

    public void setPrice(String price) {
        this.price = price;
   }

    public String getIsbn() {
        return isbn;
   }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
   }

    public Integer getPageNum() {
        return pageNum;
   }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
   }

    public String getBookPic() {
        return bookPic;
   }

    public void setBookPic(String bookPic) {
        this.bookPic = bookPic;
   }

    @Override
    public String toString() {
        return "BookInfo{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", price='" + price + '\'' +
                ", isbn='" + isbn + '\'' +
                ", pageNum=" + pageNum +
                ", bookPic='" + bookPic + '\'' +
                '}';
    }
}

/*List columns as follows:
"id", "book_name", "author", "publisher", "publish_date", "price", "isbn", 
"page_num", "book_pic"
*/