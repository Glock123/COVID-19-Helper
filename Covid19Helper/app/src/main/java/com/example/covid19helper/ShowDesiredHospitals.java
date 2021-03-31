package com.example.covid19helper;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowDesiredHospitals extends AppCompatActivity {
    public static ArrayList<HospitalData> hospitalData;
    private TextView mState, mDistrict;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_desired_hospitals);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mState = findViewById(R.id.statename);
        mDistrict = findViewById(R.id.districtname);

        String state="", district="";

        Intent in = getIntent();
        if(in != null) {
            state = in.getStringExtra("state");
            district = in.getStringExtra("district");
        }
        assert in != null;
        hospitalData = (ArrayList<HospitalData>) in.getSerializableExtra("hospitalData");

        mState.append(state);
        mDistrict.append(district);

        // Setting up the recycler view
        mRecyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
