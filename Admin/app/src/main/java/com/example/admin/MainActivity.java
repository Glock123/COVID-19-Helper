package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mUsername, mPassword;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mUsername = findViewById(R.id.login_username);
        mPassword = findViewById(R.id.login_password);

        checkIfLoggedIn();

        db = FirebaseFirestore.getInstance();
    }

    private void checkIfLoggedIn() {
        // We check if user is logged in, if so, directly we move to Dashboard
        SessionManager mManager = new SessionManager(this);
        AdminLoginData loggedInUser = mManager.getSession();

        // If returned value is not NA, means already user is logged in, so we directly move to dashboard.
        if(loggedInUser != null) {
            Intent in = new Intent(this, Dashboard.class);
            in.putExtra("adminData", loggedInUser);
            startActivity(in);
        }
    }

    public void onRegistration(View view) {
        Intent in = new Intent(this, Registration.class);
        mUsername.setText("");
        mPassword.setText("");

        startActivity(in);
    }

    public void onLogin(View view) {
        final String username = mUsername.getText().toString();
        final String password = mPassword.getText().toString();

        db.collection(Registration.ADMIN_LOGIN_COLLECTION)
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()) {
                            // No such username exists and we cannot allow to login
                            Toast.makeText(MainActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();

                        }

                        else {
                            // We have to check for validity of password.
                            List<AdminLoginData> data = task.getResult().toObjects(AdminLoginData.class);

                            // I have forced to have only one username of a kind, so size of data must be 1.
                            AdminLoginData temp = data.get(0);

                            // Now, I check if password corresponding to username saved in database is same as entered
                            if(temp.getPassword().compareTo(password) == 0) {

                                // Correct password entered, proceed to dashboard

                                // Shared Preferences!!!
                                SessionManager mManager =new SessionManager(MainActivity.this);
                                mManager.saveSession(temp);

                                // Flushing the username and password entered
                                mUsername.setText("");
                                mPassword.setText("");

                                // Proceed to dashboard
                                Intent in = new Intent(MainActivity.this, Dashboard.class);
                                in.putExtra("adminData", temp);
                                startActivity(in);

                            }

                            else {
                                Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Some error has occurred, try again", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
