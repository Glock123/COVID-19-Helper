package com.example.covid19helper;

import android.os.Parcel;
import android.os.Parcelable;

public class UserLoginData implements Parcelable {
    private String username, name, phoneno, password, userId;

    public UserLoginData() {}

    public UserLoginData(String username, String name, String phoneno, String password, String userId) {
        this.username = username;
        this.name = name;
        this.phoneno = phoneno;
        this.password = password;
        this.userId = userId;
    }

    protected UserLoginData(Parcel in) {
        username = in.readString();
        name = in.readString();
        phoneno = in.readString();
        password = in.readString();
        userId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(phoneno);
        dest.writeString(password);
        dest.writeString(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserLoginData> CREATOR = new Creator<UserLoginData>() {
        @Override
        public UserLoginData createFromParcel(Parcel in) {
            return new UserLoginData(in);
        }

        @Override
        public UserLoginData[] newArray(int size) {
            return new UserLoginData[size];
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

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }
}
