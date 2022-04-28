package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model;

import java.io.Serializable;

public class User implements Serializable {
    public String email;
    public String username;
    public String password;
    public String image;

    public String userID;
    public String lastMessage;


    public User(String email, String username, String password, String image, String userID, String lastMessage) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.image = image;
        this.userID = userID;
        this.lastMessage = lastMessage;
    }

    public User(){

    }
    public String getUserID() {
        return userID;
    }

    //sign up
    public User(String email, String username, String password, String image) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    @Override
    public String toString() {
        return "User{" +
                ", userID='" + userID + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}
