package com.ass2.i190417_i192048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainChatScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ChatModal> ls = new ArrayList<>();
    ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_screen);
        recyclerView = findViewById(R.id.recycler_view);

        chatAdapter = new ChatAdapter(ls,MainChatScreen.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainChatScreen.this);
        recyclerView.setLayoutManager(layoutManager);
        ls.add(new ChatModal("Someone","0303","image"));
        ls.add(new ChatModal("Someone","0303","image"));
        ls.add(new ChatModal("Someone","0303","image"));
        ls.add(new ChatModal("Someone","0303","image"));
        System.out.println(ls.get(0));
        recyclerView.setAdapter(chatAdapter);



    }
}