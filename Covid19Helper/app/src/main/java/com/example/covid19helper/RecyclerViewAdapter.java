package com.example.covid19helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private FirebaseFirestore db;

    public RecyclerViewAdapter(Context context) {
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(ShowDesiredHospitals.hospitalData.get(position).getNameHospital());
        holder.numBeds.setText(ShowDesiredHospitals.hospitalData.get(position).getNumBeds() + " Beds Available");
        holder.desc.setText(ShowDesiredHospitals.hospitalData.get(position).getDescHospital());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(context);
                mAlertDialog.setTitle("Add to favourites?");
                mAlertDialog.setMessage("Get to notify on number of beds");

                mAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection(Dashboard.HOSPITAL_COLLECTION)
                                .whereEqualTo("state", ShowDesiredHospitals.hospitalData.get(position).getState())
                                .whereEqualTo("district", ShowDesiredHospitals.hospitalData.get(position).getDistrict())
                                .whereEqualTo("nameHospital", ShowDesiredHospitals.hospitalData.get(position).getNameHospital())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            String id = task.getResult().getDocuments().get(0).getId();
                                            DocumentReference ref;

                                            // Adding current logged in user to hospital database
                                            ref = db.collection(Dashboard.HOSPITAL_COLLECTION).document(id);
                                            final Map<String, Object> map = new HashMap<>();
                                            map.put("userWhoLikeThisHospital", FieldValue.arrayUnion(Dashboard.currentUser.getUserId()));
                                            ref.update(map);

                                            // Now adding hospital id to user's favourites
                                            ref = db.collection(Registration.USER_LOGIN_COLLECTION).document(Dashboard.currentUser.getUserId());
                                            final Map<String, Object> mmap = new HashMap<>();
                                            mmap.put("FavouriteHospitals", FieldValue.arrayUnion(id));
                                            ref.update(mmap);

                                            Toast.makeText(context, "Hospital added to favourites!!!", Toast.LENGTH_SHORT).show();
                                        }

                                        else {
                                            Toast.makeText(context, "No hospital found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });

                mAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });

                mAlertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ShowDesiredHospitals.hospitalData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, numBeds, desc;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            numBeds = itemView.findViewById(R.id.num_beds);
            desc = itemView.findViewById(R.id.desc);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
