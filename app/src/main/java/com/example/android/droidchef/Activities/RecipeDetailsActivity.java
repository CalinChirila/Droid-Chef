package com.example.android.droidchef.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.droidchef.CustomObjects.Recipe;
import com.example.android.droidchef.Fragments.RecipeDetailsFragment;
import com.example.android.droidchef.R;

import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity{

    public static boolean isTwoPane;
    private Recipe mCurrentRecipe;

    private static final String RECIPE_SAVED_STATE = "recipeSavedState";
    RecipeDetailsFragment mRecipeDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null){
            mCurrentRecipe = savedInstanceState.getParcelable(RECIPE_SAVED_STATE);
        } else {
            mCurrentRecipe = getIntent().getParcelableExtra(MainActivity.RECIPE_PARCEL);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.RECIPE_PARCEL, mCurrentRecipe);

        mRecipeDetailsFragment = new RecipeDetailsFragment();
        mRecipeDetailsFragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.recipe_details_container, mRecipeDetailsFragment)
                .addToBackStack(null)
                .commit();

        // If this layout exists, it means we deal with a tablet user
        if(findViewById(R.id.tablet_step_details_layout) != null){
            isTwoPane = true;
        } else {
            isTwoPane = false;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_SAVED_STATE, mCurrentRecipe);

    }
}
