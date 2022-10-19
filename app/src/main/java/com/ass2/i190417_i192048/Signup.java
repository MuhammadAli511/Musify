package com.ass2.i190417_i192048;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Signup extends AppCompatActivity {
    ImageView profilePic;
    EditText name, email, password;
    RadioGroup genderRadioGroup;
    RadioButton genderRadioButton;
    Button signup;
    CheckBox terms;
    FirebaseAuth mAuth;
    TextView signin;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        signup = findViewById(R.id.signup);
        terms = findViewById(R.id.checkbox);
        signin = findViewById(R.id.signin);
        profilePic = findViewById(R.id.profilePic);
        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 80);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!terms.isChecked()){
                    Toast.makeText(Signup.this, "Please accept terms and conditions", Toast.LENGTH_LONG).show();
                }
                else{
                    StorageReference imageRef = storageRef.child("images/" + email.getText().toString());
                    UploadTask uploadTask = imageRef.putFile(imageURI);
                    int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                    genderRadioButton = findViewById(selectedId);
                    String gender = genderRadioButton.getText().toString();
                    if (name.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("")){
                        Toast.makeText(Signup.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                    }
                    else{
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(Signup.this, "Image upload failed", Toast.LENGTH_LONG).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(Signup.this, "Image uploaded successfully", Toast.LENGTH_LONG).show();
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadURL) {
                                        String imageURL = downloadURL.toString();
                                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    @Override
                                                    public void onSuccess(AuthResult authResult) {
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                .setDisplayName(name.getText().toString())
                                                                .setPhotoUri(Uri.parse(imageURL))
                                                                .build();
                                                        user.updateProfile(profileUpdates)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(Signup.this, "User created successfully", Toast.LENGTH_LONG).show();
                                                                            Intent intent = new Intent(Signup.this, Signin.class);
                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                            startActivity(intent);
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Signup.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                    }
                                });




                            }
                        });

                    }







                }


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Signin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Signup.this.finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 80 && resultCode == RESULT_OK) {
            profilePic.setImageURI(data.getData());
            imageURI = data.getData();
        }
    }


}