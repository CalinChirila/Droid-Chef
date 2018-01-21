package com.example.android.droidchef.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.droidchef.Activities.MainActivity;
import com.example.android.droidchef.Activities.RecipeDetailsActivity;
import com.example.android.droidchef.Activities.StepDetailsActivity;
import com.example.android.droidchef.Adapters.RecipeDetailsAdapter;
import com.example.android.droidchef.CustomObjects.Recipe;
import com.example.android.droidchef.CustomObjects.Step;
import com.example.android.droidchef.R;

/**
 * Created by Astraeus on 1/19/2018.
 */

public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.RecipeDetailsClickHandler{


    public static final String STEP_PARCEL = "stepParcel";

    private LinearLayoutManager mLayoutManager;
    private Recipe mRecipeData;
    private Bundle mBundle;
    private RecyclerView mRecyclerView;
    private RecipeDetailsAdapter mAdapter;

    private ExoPlayerFragment mPlayerFragment;
    private StepDescriptionFragment mDescriptionFragment;
    private String mVideoString;
    private String mDescriptionString;


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

        mAdapter = new RecipeDetailsAdapter(mRecipeData, this);
        mRecyclerView.setAdapter(mAdapter);


        return mRecyclerView;
    }



    @Override
    public void onClick(Step step) {

        if(RecipeDetailsActivity.isTwoPane){
            // Tablet
            handleTabletCase(step);
        } else {
            // Phone
            handlePhoneCase(step);
        }
    }


    public void handlePhoneCase(Step step){
        Intent intent = new Intent(getActivity(), StepDetailsActivity.class);
        intent.putExtra(MainActivity.RECIPE_PARCEL, mRecipeData);
        intent.putExtra(STEP_PARCEL, step);
        startActivity(intent);
    }



    /**
     * Helper method that handles the case where the user is on a tablet
     * @param step the selected step
     */
    public void handleTabletCase(Step step){
        // Get the necessary data
        String videoString = step.getStepVideoURLString();
        String thumbnailString = step.getStepThumbnailURLString();
        String descriptionString = step.getStepDescription();

        Uri mediaUri;
        // Handle the video url in the wrong json field
        if((videoString == null || TextUtils.isEmpty(videoString)) && thumbnailString.contains(".mp4")){
            mediaUri = Uri.parse(thumbnailString);
        } else {
            mediaUri = Uri.parse(videoString);
        }
        FragmentManager fm = getFragmentManager();

        // If these strings are null, the user clicked a recipe step for the first time
        if(mVideoString == null && mDescriptionString == null){
            // Create new fragments and add them

            mPlayerFragment = new ExoPlayerFragment();
            mDescriptionFragment = new StepDescriptionFragment();

            mPlayerFragment.setMediaUri(mediaUri);
            mDescriptionFragment.setStepDescription(descriptionString);

            fm.beginTransaction()
                    .add(R.id.container_exo_player, mPlayerFragment)
                    .add(R.id.container_step_description, mDescriptionFragment)
                    .commit();

        } else {
            ExoPlayerFragment newPlayerFragment = new ExoPlayerFragment();
            StepDescriptionFragment newDescriptionFragment = new StepDescriptionFragment();

            newPlayerFragment.setMediaUri(mediaUri);
            newDescriptionFragment.setStepDescription(descriptionString);

            fm.beginTransaction()
                    .replace(R.id.container_exo_player, newPlayerFragment)
                    .replace(R.id.container_step_description, newDescriptionFragment)
                    .commit();

            mPlayerFragment = newPlayerFragment;
            mDescriptionFragment = newDescriptionFragment;
        }

        mVideoString = mediaUri.toString();
        mDescriptionString = descriptionString;

        if(mVideoString == null || TextUtils.isEmpty(mVideoString)){
            fm.beginTransaction().hide(mPlayerFragment).commit();
        }
    }

}
