package com.ass2.i190417_i192048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainScreen extends AppCompatActivity {
    LinearLayout liked, add, search, listenlater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        liked = findViewById(R.id.liked);
        add = findViewById(R.id.add);
        search = findViewById(R.id.search);
        listenlater = findViewById(R.id.listenlater);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, Add.class));
            }
        });

    }
}