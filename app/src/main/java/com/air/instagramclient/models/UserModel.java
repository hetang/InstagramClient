package com.air.instagramclient.models;

/**
 * Created by hetashah on 2/6/15.
 */
public class UserModel {
    private String name;
    private String profileImgURL;
    private String id;
    private String fullName;

    /*public String toString() {
        return "[ id = " + id + " name = " + name + " fullName = " + fullName + " profileImgURL = " + profileImgURL + " ]";
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImgURL() {
        return profileImgURL;
    }

    public void setProfileImgURL(String profileImgURL) {
        this.profileImgURL = profileImgURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
