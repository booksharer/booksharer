package com.booksharer.entity;

import java.io.Serializable;

public class BookInfo  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String bookName;
    private String originName;
    private String author;
    private String translateAuthor;
    private String publisher;
    private String publishDate;
    private String price;
    private String isbn;
    private String pageNum;
    private String bookPic;
    private String zhuangZhen;
    private String congShu;
    private Double score;
    private String contentIntro;
    private String authorIntro;
    private String menu;
    private String tags;


    public String getId() {
        return id;
   }

    public void setId(String id) {
        this.id = id;
   }

    public String getBookName() {
        return bookName;
   }

    public void setBookName(String bookName) {
        this.bookName = bookName;
   }

    public String getOriginName() {
        return originName;
   }

    public void setOriginName(String originName) {
        this.originName = originName;
   }

    public String getAuthor() {
        return author;
   }

    public void setAuthor(String author) {
        this.author = author;
   }

    public String getTranslateAuthor() {
        return translateAuthor;
   }

    public void setTranslateAuthor(String translateAuthor) {
        this.translateAuthor = translateAuthor;
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

    public String getPageNum() {
        return pageNum;
   }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
   }

    public String getBookPic() {
        return bookPic;
   }

    public void setBookPic(String bookPic) {
        this.bookPic = bookPic;
   }

    public String getZhuangZhen() {
        return zhuangZhen;
   }

    public void setZhuangZhen(String zhuangZhen) {
        this.zhuangZhen = zhuangZhen;
   }

    public String getCongShu() {
        return congShu;
   }

    public void setCongShu(String congShu) {
        this.congShu = congShu;
   }

    public Double getScore() {
        return score;
   }

    public void setScore(Double score) {
        this.score = score;
   }

    public String getContentIntro() {
        return contentIntro;
   }

    public void setContentIntro(String contentIntro) {
        this.contentIntro = contentIntro;
   }

    public String getAuthorIntro() {
        return authorIntro;
   }

    public void setAuthorIntro(String authorIntro) {
        this.authorIntro = authorIntro;
   }

    public String getMenu() {
        return menu;
   }

    public void setMenu(String menu) {
        this.menu = menu;
   }

    public String getTags() {
        return tags;
   }

    public void setTags(String tags) {
        this.tags = tags;
   }

    @Override
    public String toString() {
        return "BookInfo{" +
                "id='" + id + '\'' +
                ", bookName='" + bookName + '\'' +
                ", originName='" + originName + '\'' +
                ", author='" + author + '\'' +
                ", translateAuthor='" + translateAuthor + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", price='" + price + '\'' +
                ", isbn='" + isbn + '\'' +
                ", pageNum='" + pageNum + '\'' +
                ", bookPic='" + bookPic + '\'' +
                ", zhuangZhen='" + zhuangZhen + '\'' +
                ", congShu='" + congShu + '\'' +
                ", score=" + score +
                ", contentIntro='" + contentIntro + '\'' +
                ", authorIntro='" + authorIntro + '\'' +
                ", menu='" + menu + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}

/*List columns as follows:
"id", "book_name", "origin_name", "author", "translate_author", "publisher", "publish_date", 
"price", "isbn", "page_num", "book_pic", "zhuang_zhen", "cong_shu", "score", 
"content_intro", "author_intro", "menu", "tags"
*/