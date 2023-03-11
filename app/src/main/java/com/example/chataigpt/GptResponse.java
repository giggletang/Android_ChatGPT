package com.example.chataigpt;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GptResponse {
    @SerializedName("choices")
    private List<Choice> choices;

    public List<Choice> getChoices(){
        return choices;
    }

    public class Choice{
        @SerializedName("message")
        private Message message;

        public Message getMessage(){
            return message;
        }
    }

}
