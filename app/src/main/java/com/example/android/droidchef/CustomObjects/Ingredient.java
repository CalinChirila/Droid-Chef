package com.example.android.droidchef.CustomObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Astraeus on 1/10/2018.
 */

public class Ingredient implements Parcelable{

    // The member variables for the Ingredient class
    private double mQuantity;
    private String mMeasure;
    private String mName;

    // The constructor
    public Ingredient(String name, double quantity, String measure){
        mName = name;
        mMeasure = measure;
        mQuantity = quantity;
    }

    protected Ingredient(Parcel in) {
        mQuantity = in.readDouble();
        mMeasure = in.readString();
        mName = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    // Getter methods for name, quantity and measure
    public String getIngredientName(){ return mName; }
    public double getIngredientQuantity(){ return mQuantity; }
    public String getIngredientMeasure(){ return mMeasure; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mQuantity);
        parcel.writeString(mMeasure);
        parcel.writeString(mName);
    }
}
