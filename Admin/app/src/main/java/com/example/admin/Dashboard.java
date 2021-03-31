package com.example.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class Dashboard extends AppCompatActivity {

    private LinearLayout mLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent in = getIntent();
        AdminLoginData currentAdmin = in.getParcelableExtra("adminData");

        mLinearLayout = findViewById(R.id.dashboard_linear_layout);
        setBackGroundImage();
    }

    private void setBackGroundImage() {
        String url = "https://picsum.photos/600";

        Glide.with(this)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Caching nothing, hence taking random image each time.
                .skipMemoryCache(true).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mLinearLayout.setBackground(resource);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onViewHospital(View view) {
        Intent in = new Intent(this, ViewHospitals.class);
        startActivity(in);
    }

    public void onAddHospital(View view) {
        Intent in = new Intent(this, AddHospital.class);
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

                mAlertDialog.show();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
