package com.example.solutions;

import android.content.Intent;

public class Comment {
    private String answer;
    private String email;
    private String profilePic;
    private String uid;
    private String username;
    private String postID;
    private String dateTime;

    public Comment() {
    }

    public Comment(String answer, String email, String profilePic, String uid, String username, String postID, String dateTime) {
        this.answer = answer;
        this.email = email;
        this.profilePic = profilePic;
        this.uid = uid;
        this.username = username;
        this.postID = postID;
        this.postID = dateTime;
    }

    public String getAnswer() {
        return answer;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
