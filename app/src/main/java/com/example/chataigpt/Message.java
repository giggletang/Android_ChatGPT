package com.example.chataigpt;

public class Message {
    public static String SENT_BY_US = "ME";
    public static  String SENT_BY_GPT = "BOT";

    String message;
    String sentby;

    public Message(String message, String sentby) {
        this.message = message;
        this.sentby = sentby;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentby() {
        return sentby;
    }

    public void setSentby(String sentby) {
        this.sentby = sentby;
    }
}
