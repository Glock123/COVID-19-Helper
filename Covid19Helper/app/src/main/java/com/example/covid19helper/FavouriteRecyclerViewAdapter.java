package com.example.covid19helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FavouriteRecyclerViewAdapter extends RecyclerView.Adapter< FavouriteRecyclerViewAdapter.MyFavouriteViewHolder > {

    private Context context;
    private FirebaseFirestore db;

    public FavouriteRecyclerViewAdapter(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public FavouriteRecyclerViewAdapter.MyFavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.favourite_cardview, parent, false);

        return new MyFavouriteViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull final MyFavouriteViewHolder holder, final int position) {

        String hospId = ViewFavourites.hospId.get(position);
        db.collection(Dashboard.HOSPITAL_COLLECTION).document(hospId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snap = task.getResult();
                        holder.name.setText(snap.getString("nameHospital"));
                        holder.numBeds.setText(snap.getString("numBeds") + " Beds Available");
                        holder.desc.setText(snap.getString("descHospital"));
                        holder.address.setText(snap.getString("district") + ", " + snap.getString("state"));
                    }
                });


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(context);
                mAlertDialog.setTitle("Remove From Favourites?");


                mAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /**
                         * Delete hospital from userData
                         * Delete user from hospital data
                         */

                        // 1. Deleting user from hospital data
                        DocumentReference ref = db.collection(Dashboard.HOSPITAL_COLLECTION).document(ViewFavourites.hospId.get(position));
                        final Map<String, Object> map = new HashMap<>();
                        map.put("userWhoLikeThisHospital", FieldValue.arrayRemove(Dashboard.currentUser.getUserId()));
                        ref.update(map);

                        // 2. Deleting hospital from user data
                        ref = db.collection(Dashboard.USER_LOGIN_COLLECTION).document(Dashboard.currentUser.getUserId());
                        final Map<String, Object> mmap = new HashMap<>();
                        mmap.put("FavouriteHospitals", FieldValue.arrayRemove(ViewFavourites.hospId.get(position)));
                        ref.update(mmap);
                        Toast.makeText(context, "Hospital deleted from favourites!!!", Toast.LENGTH_LONG).show();

                        Intent in = new Intent(context, Dashboard.class);
                        in.putExtra("userData", Dashboard.currentUser);
                        context.startActivity(in);


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

    @SuppressLint("LongLogTag")
    @Override
    public int getItemCount() {
        return ViewFavourites.hospId.size();

    }

    public static class MyFavouriteViewHolder extends RecyclerView.ViewHolder {

        public TextView name, numBeds, desc, address;
        public Button remove;

        public MyFavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.hosp_name);
            numBeds = itemView.findViewById(R.id.hosp_numbeds);
            desc = itemView.findViewById(R.id.hosp_desc);
            remove = itemView.findViewById(R.id.remove);
            address = itemView.findViewById(R.id.hosp_address);
        }
    }
}
