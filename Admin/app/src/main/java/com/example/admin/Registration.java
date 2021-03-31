package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private EditText mName, mUsername, mPassword, mPhoneNo, mConfirmPassword;
    private FirebaseFirestore db;

    public static final String ADMIN_LOGIN_COLLECTION = "adminLoginDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mName = findViewById(R.id.registration_name);
        mUsername = findViewById(R.id.registration_username);
        mPhoneNo = findViewById(R.id.registration_phone_no);
        mPassword = findViewById(R.id.registration_password);
        mConfirmPassword = findViewById(R.id.registration_confirm_password);

        db = FirebaseFirestore.getInstance();

    }

    public void onRegisterMe(View view) {
        String name = mName.getText().toString().trim();
        String username = mUsername.getText().toString().trim();
        String phoneno = mPhoneNo.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmpassword = mConfirmPassword.getText().toString().trim();

        if(name.length()==0 || username.length()==0 || phoneno.length()==0 || password.length()==0 || confirmpassword.length()==0) {
            Toast.makeText(this, "No field can be left empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.compareTo(confirmpassword) != 0) {
            Toast.makeText(this, "Password should be same in two fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6) {
            Toast.makeText(this, "Password should have minimum 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        final AdminLoginData userDetails = new AdminLoginData(username, name, phoneno, password);

        // Checking if admin already present in database
        db.collection(ADMIN_LOGIN_COLLECTION)
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                Toast.makeText(Registration.this, "Admin with same username already exists!", Toast.LENGTH_SHORT).show();
                                Log.e("User already added: ", "TRUE");
                            }

                            else {
                                // Username is unique,  we can add it to database safely
                                db.collection(ADMIN_LOGIN_COLLECTION)
                                        .add(userDetails)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(Registration.this, "User Added Successfully!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Registration.this, "Error adding new user, try again!\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.e("ADMIN NOT ADDED: ", e.getMessage());
                                            }
                                        });

                            }
                        }
                    }
                });



    }
}
