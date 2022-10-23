package com.ass2.i190417_i192048.ChatApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.i190417_i192048.Adapters.MessageAdapter;
import com.ass2.i190417_i192048.Models.Messages;
import com.ass2.i190417_i192048.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    ImageView backButton, userImage, sendMsg;
    TextView userName, status;
    EditText messageToSend;
    FirebaseAuth mAuth;
    RecyclerView chatDetailRecyclerView;
    List<Messages> messagesList;
    MessageAdapter messageAdapter;
    String senderId1;
    String receiverId1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        backButton = findViewById(R.id.backButton);
        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        sendMsg = findViewById(R.id.sendMsg);
        messageToSend = findViewById(R.id.messageToSend);
        status = findViewById(R.id.status);

        mAuth = FirebaseAuth.getInstance();
        chatDetailRecyclerView = findViewById(R.id.chatDetailRecyclerView);
        messagesList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messagesList, this);
        chatDetailRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatDetailRecyclerView.setLayoutManager(layoutManager);


        final String senderId = mAuth.getCurrentUser().getUid();
        String receiverId = getIntent().getStringExtra("userID");
        String receiverName = getIntent().getStringExtra("userName");
        String receiverImage = getIntent().getStringExtra("profileURL");

        userName.setText(receiverName);
        Glide.with(this).load(receiverImage).into(userImage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String senderRoom = senderId + receiverId;
        String receiverRoom = receiverId + senderId;
        senderId1 = senderId;
        receiverId1 = receiverId;

        // check for realtime updates on user status
        db.getReference().child("UsersStatus").child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String status1 = snapshot.getValue(String.class);
                    status.setText(status1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db.getReference().child("UsersStatus").child(receiverId1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String status1 = task.getResult().getValue(String.class);
                    status.setText(status1);
                }
            }
        });



        db.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Messages messages = snapshot1.getValue(Messages.class);
                    messagesList.add(messages);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageToSend.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(ChatDetailActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                    return;
                }
                Messages messages = new Messages(senderId, message);
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
                String dateText = formatter.format(cal.getTime());
                messages.setTimestamp(dateText);
                messageToSend.setText("");

                db.getReference().child("Chats").child(senderRoom).push().setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.getReference().child("Chats").child(receiverRoom).push().setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();
        db.getReference().child("UsersStatus").child(receiverId1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String status1 = task.getResult().getValue(String.class);
                    status.setText(status1);
                }
            }
        });

    }
}