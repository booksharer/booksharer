package com.booksharer.entity;

import java.io.Serializable;

public class Comment  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer parentId;
    private String commentContent;
    private String commentDate;
    private Integer commentUserId;
    private Integer commentType;
    private Integer commenttedId;
    private Integer commenttedId_1;


    public Integer getId() {
        return id;
   }

    public void setId(Integer id) {
        this.id = id;
   }

    public Integer getParentId() {
        return parentId;
   }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
   }

    public String getCommentContent() {
        return commentContent;
   }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
   }

    public String getCommentDate() {
        return commentDate;
   }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
   }

    public Integer getCommentUserId() {
        return commentUserId;
   }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
   }

    public Integer getCommentType() {
        return commentType;
   }

    public void setCommentType(Integer commentType) {
        this.commentType = commentType;
   }

    public Integer getCommenttedId() {
        return commenttedId;
   }

    public void setCommenttedId(Integer commenttedId) {
        this.commenttedId = commenttedId;
   }

    public Integer getCommenttedId_1() {
        return commenttedId_1;
   }

    public void setCommenttedId_1(Integer commenttedId_1) {
        this.commenttedId_1 = commenttedId_1;
   }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", commentContent='" + commentContent + '\'' +
                ", commentDate='" + commentDate + '\'' +
                ", commentUserId=" + commentUserId +
                ", commentType=" + commentType +
                ", commenttedId=" + commenttedId +
                ", commenttedId_1=" + commenttedId_1 +
                '}';
    }
}

/*List columns as follows:
"id", "parent_id", "comment_content", "comment_date", "comment_user_id", "comment_type", "commentted_id", 
"commentted_id_1"
*/