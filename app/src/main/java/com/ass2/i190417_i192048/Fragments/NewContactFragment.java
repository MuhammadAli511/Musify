package com.ass2.i190417_i192048.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ass2.i190417_i192048.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewContactFragment extends Fragment {


    public NewContactFragment() {
        // Required empty public constructor
    }


    EditText phoneNum;
    Button addContact;
    FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phoneNum = view.findViewById(R.id.phoneNumOne);
        addContact = view.findViewById(R.id.addContact);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get receiver user
                String phoneNumStr = phoneNum.getText().toString();
                Map<String, Object> nestedData = new HashMap<>();
                nestedData.put("phoneNum", phoneNumStr);
                db.collection("Users").whereEqualTo("phoneNum", phoneNumStr).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            String receiverUser = task.getResult().getDocuments().get(0).getId();
                            // Add to contacts
                            //db.collection("Users").document(currentUser).collection("Contacts").document(receiverUser).set(nestedData);
                            db.collection("Users").document(currentUser).update("Contacts", nestedData);
                            // get users phone number with given id
                            db.collection("Users").document(currentUser).get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot document = task1.getResult();
                                    if (document.exists()) {
                                        String receiverPhoneNum = document.getString("phoneNum");
                                        Map<String, Object> nestedData1 = new HashMap<>();
                                        nestedData1.put("phoneNum", receiverPhoneNum);
                                        // Add to contacts
                                        //db.collection("Users").document(receiverUser).collection("Contacts").document(currentUser).set(nestedData);
                                        db.collection("Users").document(receiverUser).update("Contacts", nestedData1);
                                        Toast.makeText(getContext(), "Contact added successfully", Toast.LENGTH_SHORT).show();
                                        phoneNum.setText("");
                                    } else {
                                        Toast.makeText(getContext(), "No such document", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Failed to get document", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                //db.collection("Contacts").document(currentUser).collection("Contacts").document(phoneNumStr).set(phoneNumStr);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_contact, container, false);
    }
}