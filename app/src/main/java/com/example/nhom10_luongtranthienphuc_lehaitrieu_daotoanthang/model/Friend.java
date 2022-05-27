package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model;

public class Friend {
    String profileUrl, username;

    public Friend(){

    }

    public Friend(String profileUrl, String username) {
        this.profileUrl = profileUrl;
        this.username = username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
