package com.example.android.droidchef.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.droidchef.Activities.MainActivity;
import com.example.android.droidchef.Adapters.RecipeDetailsAdapter;
import com.example.android.droidchef.CustomObjects.Recipe;
import com.example.android.droidchef.R;

/**
 * Created by Astraeus on 1/19/2018.
 */

public class RecipeDetailsFragment extends Fragment{


    public static final String STEP_PARCEL = "stepParcel";

    private LinearLayoutManager mLayoutManager;
    private Recipe mRecipeData;
    private Bundle mBundle;
    private RecyclerView mRecyclerView;


    public RecipeDetailsFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the recipe details fragment layout
        // rootView is the RecyclerView
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_recipe_details);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mBundle = getArguments();
        mRecipeData = mBundle.getParcelable(MainActivity.RECIPE_PARCEL);

        RecipeDetailsAdapter adapter = new RecipeDetailsAdapter(mRecipeData);
        mRecyclerView.setAdapter(adapter);

        return mRecyclerView;
    }

}
