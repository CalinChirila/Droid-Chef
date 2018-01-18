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


    public static final String STEP_BUNDLE = "step";
    public static final String MY_FRAGMENT = "fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);


        // If the phone is in landscape mode, hide the toolbar
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE
                && savedInstanceState != null) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            mExoPlayerFragment =(ExoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, MY_FRAGMENT);
            mCurrentStep = savedInstanceState.getParcelable(STEP_BUNDLE);
            loadStepInfo(mCurrentStep);
        } else {


            mCurrentRecipe = getIntent().getParcelableExtra(MainActivity.RECIPE_PARCEL);

            // The logic for the next button
            mSteps = mCurrentRecipe.getRecipeSteps();


            if (getIntent().hasExtra(RecipeDetailsActivity.STEP_PARCEL) && savedInstanceState == null) {

                mCurrentStep = getIntent().getParcelableExtra(RecipeDetailsActivity.STEP_PARCEL);

                // Load the information of the chosen step
                loadStepInfo(mCurrentStep);
            }

            // Set the behaviour of the Previous button ------------------
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadPreviousStep();
                }
            });

            // Set the behaviour of the Next button ----------------------
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadNextStep();
                }
            });
        }
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

        // If the user chose a step, load the necessary information into fragments
        if(mExoPlayerFragment == null && mStepDescriptionFragment == null) {
            mExoPlayerFragment = new ExoPlayerFragment();
            mStepDescriptionFragment = new StepDescriptionFragment();

            mStepDescriptionFragment.setStepDescription(description);
            mExoPlayerFragment.setMediaUri(mediaUri);

            fm.beginTransaction()
                    .add(R.id.container_exo_player, mExoPlayerFragment)
                    .add(R.id.container_step_description, mStepDescriptionFragment)
                    .commit();
        }
        // If the user clicked on next or previous, replace the old fragments with updated ones
        else if(mStepDescriptionFragment != null){
            ExoPlayerFragment newPlayerFragment = new ExoPlayerFragment();
            StepDescriptionFragment newDescriptionFragment = new StepDescriptionFragment();

            newDescriptionFragment.setStepDescription(description);
            newPlayerFragment.setMediaUri(mediaUri);

            fm.beginTransaction()
                    .replace(R.id.container_exo_player, newPlayerFragment)
                    .replace(R.id.container_step_description, newDescriptionFragment)
                    .commit();

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
        getSupportFragmentManager().putFragment(outState, MY_FRAGMENT, mExoPlayerFragment);
        outState.putParcelable(STEP_BUNDLE, mCurrentStep);
    }

}
