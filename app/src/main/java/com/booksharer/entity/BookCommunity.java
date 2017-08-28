package com.booksharer.entity;

import java.io.Serializable;

public class BookCommunity  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String communityName;
    private String communityDesc;
    private String communityPosition;
    private Integer communityCreatorId;
    private String communityLogo;
    private Integer communityPeopleNum;


    public Integer getId() {
        return id;
   }

    public void setId(Integer id) {
        this.id = id;
   }

    public String getCommunityName() {
        return communityName;
   }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
   }

    public String getCommunityDesc() {
        return communityDesc;
   }

    public void setCommunityDesc(String communityDesc) {
        this.communityDesc = communityDesc;
   }

    public String getCommunityPosition() {
        return communityPosition;
   }

    public void setCommunityPosition(String communityPosition) {
        this.communityPosition = communityPosition;
   }

    public Integer getCommunityCreatorId() {
        return communityCreatorId;
   }

    public void setCommunityCreatorId(Integer communityCreatorId) {
        this.communityCreatorId = communityCreatorId;
   }

    public String getCommunityLogo() {
        return communityLogo;
   }

    public void setCommunityLogo(String communityLogo) {
        this.communityLogo = communityLogo;
   }

    public Integer getCommunityPeopleNum() {
        return communityPeopleNum;
   }

    public void setCommunityPeopleNum(Integer communityPeopleNum) {
        this.communityPeopleNum = communityPeopleNum;
   }

    @Override
    public String toString() {
        return "BookCommunity{" +
                "id=" + id +
                ", communityName='" + communityName + '\'' +
                ", communityDesc='" + communityDesc + '\'' +
                ", communityPosition='" + communityPosition + '\'' +
                ", communityCreatorId=" + communityCreatorId +
                ", communityLogo='" + communityLogo + '\'' +
                ", communityPeopleNum=" + communityPeopleNum +
                '}';
    }
}

/*List columns as follows:
"id", "community_name", "community_desc", "community_position", "community_creator_id", "community_logo", "community_people_num"
*/