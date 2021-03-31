package com.example.admin;

import android.os.Parcel;
import android.os.Parcelable;

public class AdminLoginData implements Parcelable {

    private String username, name, phoneno, password;

    // Default constructor for firestick
    public AdminLoginData() {}

    public AdminLoginData(String username, String name, String phoneno, String password) {
        this.username = username;
        this.name = name;
        this.phoneno = phoneno;
        this.password = password;
    }

    protected AdminLoginData(Parcel in) {
        username = in.readString();
        name = in.readString();
        phoneno = in.readString();
        password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(phoneno);
        dest.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdminLoginData> CREATOR = new Creator<AdminLoginData>() {
        @Override
        public AdminLoginData createFromParcel(Parcel in) {
            return new AdminLoginData(in);
        }

        @Override
        public AdminLoginData[] newArray(int size) {
            return new AdminLoginData[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getPassword() {
        return password;
    }
}
