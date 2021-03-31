package com.example.covid19helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.covid19helper.UserLoginData;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String SHARED_PREF_NAME = "session";
    public static final String NAME="name", PASSWORD="password", PHONENO = "phoneno", USERNAME="username", USERID="userId";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(UserLoginData userLoginData) {
        //save session whenever user is logged in...

        // Adding the credentials to Shared Preferences
        editor.putString(USERNAME, userLoginData.getUsername());
        editor.putString(PASSWORD, userLoginData.getPassword());
        editor.putString(PHONENO, userLoginData.getPhoneno());
        editor.putString(NAME, userLoginData.getName());
        editor.putString(USERID, userLoginData.getUserId());
        editor.commit();

    }

    public UserLoginData getSession() {
        // return roll number of user whose session is saved

        if(sharedPreferences.getString(USERNAME, "NA").compareTo("NA") == 0) { return null; }

        return new UserLoginData(
                sharedPreferences.getString(USERNAME, "NA"),
                sharedPreferences.getString(NAME, "NA"),
                sharedPreferences.getString(PHONENO, "NA"),
                sharedPreferences.getString(PASSWORD, "NA"),
                sharedPreferences.getString(USERID, "NA")
        );
    }

    public void removeSession() {
        //editor.remove(ROLL_NO_OF_USER);
        editor.putString(USERNAME, "NA");
        editor.putString(PASSWORD, "NA");
        editor.putString(PHONENO, "NA");
        editor.putString(NAME, "NA");
        editor.putString(USERID, "NA");
        editor.apply();
    }


}
