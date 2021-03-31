package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddHospital extends AppCompatActivity {

    private Spinner stateSpinner, districtSpinner;
    private EditText name, desc, num;
    private StateAndDistrictData stateAndDistrictData = new StateAndDistrictData();
    private FirebaseFirestore db;

    public static final String HOSPITAL_COLLECTION = "Hospital Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        stateSpinner = findViewById(R.id.state_spinner);
        districtSpinner = findViewById(R.id.district_spinner);
        name = findViewById(R.id.hospital_name);
        desc = findViewById(R.id.hospital_description);
        num = findViewById(R.id.hospital_num_beds);
        db = FirebaseFirestore.getInstance();

        // Setting up adapter for state spinner
        ArrayAdapter<String> mStateAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                StateAndDistrictData.state);
        stateSpinner.setAdapter(mStateAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedState = adapterView.getItemAtPosition(position).toString();

                ArrayAdapter<String> mDistrictAdapter = new ArrayAdapter<>(AddHospital.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        stateAndDistrictData.getDistricts(selectedState));

                districtSpinner.setAdapter(mDistrictAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onAddHospital(View view) {
        String state = stateSpinner.getSelectedItem().toString();
        String district = districtSpinner.getSelectedItem().toString();
        String nameHospital = name.getText().toString();
        String descHospital = desc.getText().toString();
        String numBeds = num.getText().toString();

        // Checking if hospital name and num of beds are provided
        if(nameHospital.length() == 0) {
            Toast.makeText(this, "Name of Hospital cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(numBeds.length() == 0) {
            Toast.makeText(this, "Number of beds cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Data is validated and ready to be inserted in db
        final HospitalData hospitalData = new HospitalData(state, district, nameHospital, descHospital, numBeds);

        db.collection(HOSPITAL_COLLECTION)
                .whereEqualTo("state", state)
                .whereEqualTo("district", district)
                .whereEqualTo("nameHospital", nameHospital)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // If same name hospital present in same state and district, it is a duplicate and must not be added
                        if(task.isSuccessful()) {
                            if(task.getResult().isEmpty()) {
                                // Hence, no such hospital exists, we can safely add the hospital to db
                                db.collection(HOSPITAL_COLLECTION)
                                        .add(hospitalData)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(AddHospital.this,
                                                        "Hospital added successfully!",
                                                        Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AddHospital.this,
                                                        "Some error occurred, please try again...",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                            else {
                                // Same hospital exists in database, so asking to modify the current data instead
                                Toast.makeText(AddHospital.this,
                                        "This hospital already added in database,\ngo to \"View Hospitals\" to modify",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        else {
                            Toast.makeText(AddHospital.this, "Some error occurred, please try again", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddHospital.this, "Some error occurred, please try again", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
