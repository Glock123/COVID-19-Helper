package com.example.covid19helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewHospitals extends AppCompatActivity {

    private FirebaseFirestore db;
    private Spinner mState, mDistrict;
    private StateAndDistrictData stateAndDistrictData = new StateAndDistrictData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hospitals);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mState = findViewById(R.id.state_spinner_view);
        mDistrict = findViewById(R.id.district_spinner_view);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();

        // Setting up adapter for state spinner
        ArrayAdapter<String> mStateAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                StateAndDistrictData.state);
        mState.setAdapter(mStateAdapter);

        mState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedState = adapterView.getItemAtPosition(position).toString();

                ArrayAdapter<String> mDistrictAdapter = new ArrayAdapter<>(ViewHospitals.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        stateAndDistrictData.getDistricts(selectedState));

                mDistrict.setAdapter(mDistrictAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(this, Dashboard.class);
        in.putExtra("userData", Dashboard.currentUser);
        startActivity(in);
    }

    public void onSeeHospitals(View view) {
        final String state = mState.getSelectedItem().toString();
        final String district = mDistrict.getSelectedItem().toString();

        db.collection(Dashboard.HOSPITAL_COLLECTION)
                .whereEqualTo("state", state)
                .whereEqualTo("district", district)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().isEmpty()) {
                                Toast.makeText(ViewHospitals.this,
                                        "No Hospitals in this area...\nSearch for Different location",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        // There exists hospitals > 0 in state and district provided.
                        List<HospitalData> hospitalData = task.getResult().toObjects(HospitalData.class);

                        // Proceeding to ShowDesiredHospitals in recyclerview format
                        Intent in = new Intent(ViewHospitals.this, ShowDesiredHospitals.class);
                        in.putExtra("state", state);
                        in.putExtra("district", district);
                        in.putExtra("hospitalData", (ArrayList<HospitalData>)hospitalData);
                        startActivity(in);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewHospitals.this,
                                "Error in processing result,\nPlease Try Again...",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
