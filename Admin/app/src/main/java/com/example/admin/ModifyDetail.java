package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ModifyDetail extends AppCompatActivity {
    private String state, district, desc,numBeds, name;
    private EditText newNumBeds, newDesc;
    private FirebaseFirestore db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_detail);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent in = getIntent();
        state = in.getStringExtra("state");
        district = in.getStringExtra("district");
        name = in.getStringExtra("name");
        desc = in.getStringExtra("descHospital");
        numBeds = in.getStringExtra("numBeds");

        newNumBeds = findViewById(R.id.new_num_beds);
        newDesc = findViewById(R.id.new_desc);

        TextView add = findViewById(R.id.address);

        add.setText(name + ", " + district + ", " + state);
        newNumBeds.setHint(numBeds);
        newDesc.setHint(desc);

        db = FirebaseFirestore.getInstance();

    }


    public void onModify(View view) {
        String newnumbeds = newNumBeds.getText().toString();
        String newdesc = newDesc.getText().toString();

        if(newnumbeds.length() != 0) numBeds = newnumbeds;
        if(newdesc.length() != 0) desc = newdesc;

        final HospitalData hData = new HospitalData(state, district, name, desc, numBeds);

        db.collection(AddHospital.HOSPITAL_COLLECTION)
                .whereEqualTo("state", state)
                .whereEqualTo("district", district)
                .whereEqualTo("nameHospital", name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                        DocumentReference ob  = db.collection(AddHospital.HOSPITAL_COLLECTION).document(id);

                        ob.set(hData);
                        Toast.makeText(ModifyDetail.this, "Details modified successfully!!!", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(ModifyDetail.this, ViewHospitals.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ModifyDetail.this, "Some error occurred, try again...", Toast.LENGTH_LONG).show();
            }
        });

    }
}
