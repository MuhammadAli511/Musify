package com.ass2.i190417_i192048.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ass2.i190417_i192048.Models.Messages;
import com.ass2.i190417_i192048.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    List<Messages> messagesList;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public MessageAdapter(List<Messages> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            View view = View.inflate(context, R.layout.sender_msg_row, null);
            return new SenderViewHolder(view);
        } else {
            View view = View.inflate(context, R.layout.receiver_msg_row, null);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messagesList.get(position).getUserID().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("Hello", "onBindViewHolder: " + messagesList.get(position).getMessage());
        Messages messages = messagesList.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).senderMsg.setText(messages.getMessage());
            ((SenderViewHolder) holder).senderTime.setText(messages.getTimestamp().toString());
            ((SenderViewHolder) holder).senderImage.setImageResource(R.drawable.person);
        } else {
            ((ReceiverViewHolder) holder).receiverMsg.setText(messages.getMessage());
            ((ReceiverViewHolder) holder).receiverTime.setText(messages.getTimestamp().toString());
            ((ReceiverViewHolder) holder).receiverImage.setImageResource(R.drawable.ic_baseline_person_outline_24);
        }

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMsg, receiverTime;
        ImageView receiverImage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.receiverMsg);
            receiverTime = itemView.findViewById(R.id.receiverTime);
            receiverImage = itemView.findViewById(R.id.receiverImage);

        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMsg, senderTime;
        ImageView senderImage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderMsg);
            senderTime = itemView.findViewById(R.id.senderTime);
            senderImage = itemView.findViewById(R.id.senderImage);

        }
    }
}