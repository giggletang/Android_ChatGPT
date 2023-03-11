package com.example.chataigpt;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GptRequest {
    @SerializedName("model")
    private String model;

    @SerializedName("messages")
    private List<Message> messages;

    @SerializedName("max_tokens")
    private int maxToken;

    @SerializedName("temperature")
    private double temperature;

    public GptRequest(String model, List<Message> messages, int maxToken, double temperature) {
        this.model = model;
        this.messages = messages;
        this.maxToken = maxToken;
        this.temperature = temperature;
    }

}



