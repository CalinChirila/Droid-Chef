package com.example.android.droidchef.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.android.droidchef.CustomObjects.Recipe;
import com.example.android.droidchef.CustomObjects.Step;
import com.example.android.droidchef.Fragments.ExoPlayerFragment;
import com.example.android.droidchef.R;
import com.example.android.droidchef.Fragments.StepDescriptionFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class StepDetailsActivity extends AppCompatActivity {
    @BindView(R.id.button_previous)
    Button mPreviousButton;
    @BindView(R.id.button_next)
    Button mNextButton;

    private ExoPlayerFragment mExoPlayerFragment;
    private StepDescriptionFragment mStepDescriptionFragment;
    private Step mCurrentStep;
    private Recipe mCurrentRecipe;
    private ArrayList<Step> mSteps;

    private String mVideoString;
    private String mDescriptionString;


    public static final String STEP_BUNDLE = "step";
    public static final String MY_FRAGMENT_PLAYER = "fragmentPlayer";
    public static final String MY_FRAGMENT_DESCRIPTION = "fragmentDescription";
    public static final String VIDEO_BUNDLE = "videoBundle";
    public static final String DESCRIPTION_BUNDLE = "descriptionBundle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            mStepDescriptionFragment = (StepDescriptionFragment) getSupportFragmentManager().getFragment(savedInstanceState, MY_FRAGMENT_DESCRIPTION);
            mExoPlayerFragment =(ExoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, MY_FRAGMENT_PLAYER);
            mCurrentStep = savedInstanceState.getParcelable(STEP_BUNDLE);
            mVideoString = savedInstanceState.getString(VIDEO_BUNDLE);
            mDescriptionString = savedInstanceState.getString(DESCRIPTION_BUNDLE);
        }

        // If the phone is in landscape mode, hide the toolbar
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            getSupportActionBar().hide();

        } else {

            mCurrentRecipe = getIntent().getParcelableExtra(MainActivity.RECIPE_PARCEL);

            mSteps = mCurrentRecipe.getRecipeSteps();

            if (getIntent().hasExtra(RecipeDetailsActivity.STEP_PARCEL) && savedInstanceState == null) {

                mCurrentStep = getIntent().getParcelableExtra(RecipeDetailsActivity.STEP_PARCEL);

            }
        }

        if(mCurrentStep != null) loadStepInfo(mCurrentStep);

        // Set the behaviour of the Previous button ------------------------------
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPreviousStep();
            }
        });

        // Set the behaviour of the Next button ----------------------------------
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextStep();
            }
        });
    }

    private void loadPreviousStep() {
        // This is also the current position in the mSteps list
        int currentStepID = mCurrentStep.getStepID();
        if (currentStepID != 0) {
            Step newStep = mSteps.get(currentStepID - 1);

            // Update the current step
            mCurrentStep = newStep;
            // Load the new step
            loadStepInfo(newStep);
        }
    }

    private void loadNextStep() {
        // This is also the current position in the mSteps list
        int currentStepID = mCurrentStep.getStepID();

        // If the next step is within the mSteps bounds
        if (currentStepID != mSteps.size() - 1) {
            // Get the next step
            Step newStep = mSteps.get(currentStepID + 1);
            // Update the current step
            mCurrentStep = newStep;
            // Load the new step information
            loadStepInfo(newStep);
        }
    }

    //TODO: make global variables for videoString and description
    // in load step info, compare the global variables with the local ones
    // if they are different, it means that the user clicked next / previous
    //              => create new fragments and replace old ones
    // if they are the same
    //              => keep old fragments
    // if the global variables are null / empty
    //              => the user is here for the first time / create brand new fragments

    private void loadStepInfo(Step currentStep) {
        String videoString = currentStep.getStepVideoURLString();
        String thumbnailString = currentStep.getStepThumbnailURLString();
        String description = currentStep.getStepDescription();

        Uri mediaUri;

        // Check to see if the videoURL isn't the thumbnailURL by mistake
        if ((!thumbnailString.equals("") && thumbnailString.contains(".mp4")) && videoString.equals("")) {
            mediaUri = Uri.parse(thumbnailString);
        } else {
            mediaUri = Uri.parse(videoString);
        }

        FragmentManager fm = getSupportFragmentManager();

        if(mVideoString == null && mDescriptionString == null){
            mExoPlayerFragment = new ExoPlayerFragment();
            mStepDescriptionFragment = new StepDescriptionFragment();

            mStepDescriptionFragment.setStepDescription(description);
            mExoPlayerFragment.setMediaUri(mediaUri);

            fm.beginTransaction()
                    .add(R.id.container_exo_player, mExoPlayerFragment)
                    .add(R.id.container_step_description, mStepDescriptionFragment)
                    .commit();

            mVideoString = mediaUri.toString();
            mDescriptionString = description;
        } else if(mVideoString.equals(mediaUri.toString()) && mDescriptionString.equals(description)){
            // The user recreated the activity (by rotating the screen), but is at the same step

            mStepDescriptionFragment.setStepDescription(mDescriptionString);
            fm.beginTransaction()
                    .replace(R.id.container_step_description, mStepDescriptionFragment)
                    .commit();

        } else if((!mVideoString.equals(mediaUri.toString()) || !mDescriptionString.equals(description))
                && (mVideoString != null && mDescriptionString != null)){
            // The user clicked on next / previous.
            // Create new fragments with updated information and replace them.
            ExoPlayerFragment newPlayerFragment = new ExoPlayerFragment();
            StepDescriptionFragment newDescriptionFragment = new StepDescriptionFragment();

            newPlayerFragment.setMediaUri(mediaUri);
            newDescriptionFragment.setStepDescription(description);

            fm.beginTransaction()
                    .replace(R.id.container_exo_player, newPlayerFragment)
                    .replace(R.id.container_step_description, newDescriptionFragment)
                    .commit();

            mVideoString = mediaUri.toString();
            mDescriptionString = description;
            mExoPlayerFragment = newPlayerFragment;
            mStepDescriptionFragment = newDescriptionFragment;
        }

        // If there is no video or thumbnail information in the json response, hide the ExoPlayer fragment
        if (TextUtils.isEmpty(videoString) && !thumbnailString.contains(".mp4")) {
            fm.beginTransaction()
                    .hide(mExoPlayerFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, MY_FRAGMENT_PLAYER, mExoPlayerFragment);
        getSupportFragmentManager().putFragment(outState, MY_FRAGMENT_DESCRIPTION, mStepDescriptionFragment);
        outState.putString(VIDEO_BUNDLE, mVideoString);
        outState.putString(DESCRIPTION_BUNDLE, mDescriptionString);
        outState.putParcelable(STEP_BUNDLE, mCurrentStep);
    }

}
