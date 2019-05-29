package com.example.solutions;

import android.content.Intent;

public class Follow {
    private String username;
    private String title;
    private String content;
    private String keyword;
    private String email;
    private String profilePic;
    private String uid;
    private String id;
    private String dateTime;

    public Follow() {
    }

    public Follow(String username, String title, String content, String keyword, String profilePic, String email, String uid, String id, String dateTime) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.keyword = keyword;
        this.email = email;
        this.profilePic = profilePic;
        this.uid = uid;
        this.id = id;
        this.id = dateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
