package com.ass2.i190417_i192048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context mContext;
    List<Comments> commentsList;

    public CommentAdapter(Context mContext, List<Comments> commentsList) {
        this.mContext = mContext;
        this.commentsList = commentsList;
    }

    void updateList(List<Comments> list) {
        this.commentsList.clear();
        this.commentsList.addAll(list);
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.commentValue.setText(commentsList.get(position).getCommentBody());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView commentValue;

        public ViewHolder(View itemView) {
            super(itemView);
            commentValue = (TextView) itemView.findViewById(R.id.commentValue);
        }
    }


}
