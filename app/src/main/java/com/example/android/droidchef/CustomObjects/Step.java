package com.example.android.droidchef.CustomObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Astraeus on 1/10/2018.
 */

public class Step implements Parcelable{

    // Member variables for the Step class
    private String mShortDescription;
    private String mDescription;
    private String mVideoURL;
    private String mThumbnailURL;
    private int mStepID;

    // The constructor
    public Step(int stepID, String shortDescription, String description, String videoURL, String thumbnailURL){
        mStepID = stepID;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;
        mThumbnailURL = thumbnailURL;
    }

    protected Step(Parcel in) {
        mStepID = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    // Getter methods for the description, short description, video and thumbnail URLs
    public int getStepID(){ return mStepID; }
    public String getStepShortDescription(){ return mShortDescription; }
    public String getStepDescription(){ return mDescription; }
    public String getStepVideoURLString(){ return mVideoURL; }
    public String getStepThumbnailURLString() { return mThumbnailURL; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mStepID);
        parcel.writeString(mShortDescription);
        parcel.writeString(mDescription);
        parcel.writeString(mVideoURL);
        parcel.writeString(mThumbnailURL);
    }
}
