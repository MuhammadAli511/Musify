package com.ass2.i190417_i192048.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ass2.i190417_i192048.Adapters.UsersAdapter;
import com.ass2.i190417_i192048.Models.Users;
import com.ass2.i190417_i192048.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {


    public ChatsFragment() {

    }

    List<Users> list = new ArrayList<>();
    FirebaseFirestore db;
    UsersAdapter adapter;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.chatsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsersAdapter(list, getContext());
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        db.collection("Users").addSnapshotListener((value, error) -> {
            list.clear();
            for (int i = 0; i < value.size(); i++) {
                if (!value.getDocuments().get(i).getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    list.add(value.getDocuments().get(i).toObject(Users.class));
                }
            }
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}