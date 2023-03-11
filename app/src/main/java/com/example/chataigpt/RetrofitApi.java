package com.example.chataigpt;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface RetrofitApi {
    @POST("https://api.openai.com/v1/chat/completions")
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer Your_API_Key"
    })
    Call<GptResponse> getCompletion(@Body GptRequest gptRequest);
}
