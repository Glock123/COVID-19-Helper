package com.example.covid19helper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewFavourites extends AppCompatActivity {

    public static ArrayList<HospitalData> hospitalData;
    private FirebaseFirestore db;
    private RecyclerView mRecyclerView;
    private static Context context;
    public static ArrayList<String> hospId;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_favourites);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        hospitalData = new ArrayList<HospitalData>();
        mRecyclerView = findViewById(R.id.recycler_view_fav);
        context = this;
        /**
         * Create a list of all hospitals which are set as favourite by user
         * Same as ShowDesiredHospitals class
         */

        db = FirebaseFirestore.getInstance();

        DocumentReference document = db.collection(Registration.USER_LOGIN_COLLECTION).document(Dashboard.currentUser.getUserId());
        document.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            Object ids = doc.get("FavouriteHospitals");

                            // All hospital Ids
                            hospId = (ArrayList<String>) ids;
                            Log.e("SIZE OF HOSPIDs", String.valueOf(hospId.size()));
                            if(hospId.size() == 0) {
                                Toast.makeText(ViewFavourites.this, "No Favourites!!!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            mRecyclerView = findViewById(R.id.recycler_view_fav);
                            FavouriteRecyclerViewAdapter mAdapter = new FavouriteRecyclerViewAdapter(context);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewFavourites.this, "Error getting favourite hospitals, try again!!!", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(this, Dashboard.class);
        in.putExtra("userData", Dashboard.currentUser);
        startActivity(in);
    }

}
