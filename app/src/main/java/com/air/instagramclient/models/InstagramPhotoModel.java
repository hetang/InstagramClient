package com.air.instagramclient.models;

/**
 * Created by hetashah on 2/6/15.
 */
public class InstagramPhotoModel {
    private String id;
    private UserModel user;
    private String caption;
    private String imageURL;
    private int likes;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    /*public String toString() {
        return "id = " + id + " caption = " + caption + " imageURL = " + imageURL + " likes = " + likes + " user = " + user;
    }*/
}
