package com.example.android.droidchef.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.droidchef.R;

/**
 * Created by Astraeus on 1/15/2018.
 */

public class StepDescriptionFragment extends Fragment {

    private String mDescription;

    private TextView mStepDescriptionTextView;

    // Mandatory empty constructor
    public StepDescriptionFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_description, container, false);

        mStepDescriptionTextView = rootView.findViewById(R.id.fragment_step_description);
        mStepDescriptionTextView.setText(mDescription);

        return rootView;
    }

    public void setStepDescription(String description){
        mDescription = description;
    }
}
