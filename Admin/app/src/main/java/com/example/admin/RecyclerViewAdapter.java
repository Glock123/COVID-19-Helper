package com.example.admin;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(ShowDesiredHospitals.hospitalData.get(position).getNameHospital());
        holder.numBeds.setText(ShowDesiredHospitals.hospitalData.get(position).getNumBeds() + " Beds Available");
        final String description;
        if(50 > ShowDesiredHospitals.hospitalData.get(position).getDescHospital().length()) {
            description = ShowDesiredHospitals.hospitalData.get(position).getDescHospital();
        }
        else {
            description = ShowDesiredHospitals.hospitalData.get(position).getDescHospital().substring(0, 49) + "...";
        }
        holder.desc.setText(description);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(context);
                mAlertDialog.setTitle(holder.name.getText().toString());
                mAlertDialog.setMessage(ShowDesiredHospitals.hospitalData.get(position).getDescHospital());

                mAlertDialog.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent in = new Intent(context, ModifyDetail.class);
                        in.putExtra("state", ShowDesiredHospitals.hospitalData.get(position).getState());
                        in.putExtra("district", ShowDesiredHospitals.hospitalData.get(position).getDistrict());
                        in.putExtra("name", ShowDesiredHospitals.hospitalData.get(position).getNameHospital());
                        in.putExtra("descHospital", ShowDesiredHospitals.hospitalData.get(position).getDescHospital());
                        in.putExtra("numBeds", ShowDesiredHospitals.hospitalData.get(position).getNumBeds());

                        context.startActivity(in);

                    }
                });

                mAlertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Deletion of Hospital data
                        db.collection(AddHospital.HOSPITAL_COLLECTION)
                                .whereEqualTo("state", ShowDesiredHospitals.hospitalData.get(position).getState())
                                .whereEqualTo("district", ShowDesiredHospitals.hospitalData.get(position).getDistrict())
                                .whereEqualTo("nameHospital", ShowDesiredHospitals.hospitalData.get(position).getNameHospital())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                                        DocumentReference ob  = db.collection(AddHospital.HOSPITAL_COLLECTION).document(id);
                                        ob.delete();
                                        Toast.makeText(context, "Hospital Deleted Successfully!!", Toast.LENGTH_LONG).show();
                                    }
                                });

                        Intent in = new Intent(context, ViewHospitals.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(in);


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
