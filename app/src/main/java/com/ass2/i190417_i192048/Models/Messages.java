package com.ass2.i190417_i192048.Models;

public class Messages {
    String userID;
    String message;
    String timestamp;

    public Messages() {}

    public Messages(String userID, String message, String timestamp) {
        this.userID = userID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Messages(String userID, String message) {
        this.userID = userID;
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}