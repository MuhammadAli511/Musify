package com.ass2.i190417_i192048;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class IncomingCall extends  BaseActivity {

    ImageView answer,decline;
    TextView user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        answer = findViewById(R.id.pick_call);
        decline = findViewById(R.id.drop_call);
        user = findViewById(R.id.user_id);
        String mCallId = getIntent().getStringExtra(SinchService.CALL_ID);

    }
}