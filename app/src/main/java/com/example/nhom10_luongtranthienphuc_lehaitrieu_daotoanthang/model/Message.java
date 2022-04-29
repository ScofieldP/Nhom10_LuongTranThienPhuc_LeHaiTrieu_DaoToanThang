package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model;

import java.util.Date;

public class Message {
    String uID, message;

    Long timeStamp;
    int feeling;
    public Message(String uID, String message, Long timeStamp) {
        this.uID = uID;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public Message(String uID, String message) {
        this.uID = uID;
        this.message = message;
    }

    public Message(){

    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }
}
