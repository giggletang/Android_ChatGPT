package com.example.chataigpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.JsonIOException;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcometextView;
    EditText messageEditText;
    ImageButton sendButton;
    ArrayList<Message> messageList;
    ArrayList<Message> conversation;
    MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        function();

    }

    protected void function(){

        messageList = new ArrayList<>();

        if (conversation == null) {
            conversation = new ArrayList<Message>();
            conversation.add(new Message("system", "You are a helpful assistant"));
        }


        recyclerView = findViewById(R.id.recycler);
        welcometextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat("user",question);
            addToConversation("user",question);
            messageEditText.setText("");
            callApi();
            welcometextView.setVisibility(View.GONE);
        });
    }

    protected void addToChat(String role, String content){
        runOnUiThread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                messageList.add(new Message(role, content));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());

            }
        });
    }
    protected void addToConversation(String role, String content){
        conversation.add(new Message(role,content));
    }

    protected void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat("assistant", response);
        addToConversation("assistant", response);
    }

    protected void callApi(){

        messageList.add(new Message("assistant","Typing..."));

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("Retrofit", message);
            }
        });
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

        GptRequest gptRequest = new GptRequest("gpt-3.5-turbo", conversation, 4000, 0.5);
        Call<GptResponse> call = retrofitApi.getCompletion(gptRequest);
        call.enqueue(new Callback<GptResponse>() {
            @Override
            public void onResponse(@NonNull Call<GptResponse> call, @NonNull Response<GptResponse> response) {
                if(response.isSuccessful()){

                    try {
                        GptResponse res = response.body();
                        String result = res.getChoices().get(0).getMessage().getContent();
                        addResponse(result);
                    } catch (JsonIOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    addResponse("Finally Failed to load response due to " + response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<GptResponse> call, @NonNull Throwable t) {
                addResponse("Failed to load response due to "+ t.getMessage());
            }
        });
}
