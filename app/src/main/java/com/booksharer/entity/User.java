package com.booksharer.entity;

import java.io.Serializable;

public class User  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userName;
    private String email;
    private String phone;
    private String password;
    private String nickName;
    private Integer credit;
    private String qq;
    private String wechat;
    private String idCard;
    private String realName;
    private String money;
    private String position;
    private Integer sex;
    private String avatar;


    public String getId() {
        return id;
   }

    public void setId(String id) {
        this.id = id;
   }

    public String getUserName() {
        return userName;
   }

    public void setUserName(String userName) {
        this.userName = userName;
   }

    public String getEmail() {
        return email;
   }

    public void setEmail(String email) {
        this.email = email;
   }

    public String getPhone() {
        return phone;
   }

    public void setPhone(String phone) {
        this.phone = phone;
   }

    public String getPassword() {
        return password;
   }

    public void setPassword(String password) {
        this.password = password;
   }

    public String getNickName() {
        return nickName;
   }

    public void setNickName(String nickName) {
        this.nickName = nickName;
   }

    public Integer getCredit() {
        return credit;
   }

    public void setCredit(Integer credit) {
        this.credit = credit;
   }

    public String getQq() {
        return qq;
   }

    public void setQq(String qq) {
        this.qq = qq;
   }

    public String getWechat() {
        return wechat;
   }

    public void setWechat(String wechat) {
        this.wechat = wechat;
   }

    public String getIdCard() {
        return idCard;
   }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
   }

    public String getRealName() {
        return realName;
   }

    public void setRealName(String realName) {
        this.realName = realName;
   }

    public String getMoney() {
        return money;
   }

    public void setMoney(String money) {
        this.money = money;
   }

    public String getPosition() {
        return position;
   }

    public void setPosition(String position) {
        this.position = position;
   }

    public Integer getSex() {
        return sex;
   }

    public void setSex(Integer sex) {
        this.sex = sex;
   }

    public String getAvatar() {
        return avatar;
   }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
   }

}

/*List columns as follows:
"id", "user_name", "email", "phone", "password", "nick_name", "credit", 
"qq", "wechat", "id_card", "real_name", "money", "position", "sex", 
"avatar"
*/