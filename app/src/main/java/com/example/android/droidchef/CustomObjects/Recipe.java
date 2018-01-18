package com.example.android.droidchef.CustomObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Astraeus on 1/10/2018.
 */

public class Recipe implements Parcelable {

    // The member variables for the Recipe class
    private String mName;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;

    // The constructor
    public Recipe(String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps){
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
    }

    protected Recipe(Parcel in) {
        mName = in.readString();
        mIngredients = in.readArrayList(Ingredient.class.getClassLoader());
        mSteps = in.readArrayList(Step.class.getClassLoader());
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    // Getter methods for the name, list of ingredients and list of steps
    public String getRecipeName(){ return mName; }
    public ArrayList<Ingredient> getRecipeIngredients(){ return mIngredients; }
    public ArrayList<Step> getRecipeSteps(){ return mSteps; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeList(mIngredients);
        parcel.writeList(mSteps);
    }
}
