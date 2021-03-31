package com.example.covid19helper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.DatabaseMetaData;

public class Dashboard extends AppCompatActivity {

    public static UserLoginData currentUser;
    private FirebaseFirestore db;
    private TextView setUserDetails;
    public static final String HOSPITAL_COLLECTION = "Hospital Info";
    public static final String USER_LOGIN_COLLECTION = "userLoginDetails";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent in = getIntent();
        currentUser = in.getParcelableExtra("userData");
        Log.e("INSIDE DASHBOARD WITH USERID: ", currentUser.getUserId());

        db = FirebaseFirestore.getInstance();
        db.collection(Registration.USER_LOGIN_COLLECTION).document(currentUser.getUserId());

        setUserDetails = findViewById(R.id.set_user_details);
        setUserDetails.setText("Name: " + currentUser.getName() + "\nUsername: " + currentUser.getUsername());
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onViewHospital(View view) {
        Intent in = new Intent(this, ViewHospitals.class);
        startActivity(in);
    }

    public void onViewFavourites(View view) {
        Intent in = new Intent(this, ViewFavourites.class);
        startActivity(in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.logout:
                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(this);
                mAlertDialog.setTitle("Want to logout?");
                mAlertDialog.setMessage("Have to login with valid credentials again");

                mAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SessionManager mManager = new SessionManager(Dashboard.this);
                        mManager.removeSession();
                        finish();
                    }
                });
                mAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                mAlertDialog.setCancelable(true);
                mAlertDialog.show();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }



}
