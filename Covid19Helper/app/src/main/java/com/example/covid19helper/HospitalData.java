package com.example.covid19helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class HospitalData implements Parcelable, Serializable {

    private String state, district, nameHospital, descHospital, numBeds;

    // Default constructor for FireStick
    public HospitalData() {}

    public HospitalData(String state, String district, String nameHospital, String descHospital, String numBeds) {
        this.state = state;
        this.district = district;
        this.nameHospital = nameHospital;
        this.descHospital = descHospital;
        this.numBeds = numBeds;
    }

    protected HospitalData(Parcel in) {
        state = in.readString();
        district = in.readString();
        nameHospital = in.readString();
        descHospital = in.readString();
        numBeds = in.readString();
    }

    public static final Creator<HospitalData> CREATOR = new Creator<HospitalData>() {
        @Override
        public HospitalData createFromParcel(Parcel in) {
            return new HospitalData(in);
        }

        @Override
        public HospitalData[] newArray(int size) {
            return new HospitalData[size];
        }
    };

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public String getNameHospital() {
        return nameHospital;
    }

    public String getDescHospital() {
        return descHospital;
    }

    public String getNumBeds() {
        return numBeds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(state);
        parcel.writeString(district);
        parcel.writeString(nameHospital);
        parcel.writeString(descHospital);
        parcel.writeString(numBeds);
    }
}
