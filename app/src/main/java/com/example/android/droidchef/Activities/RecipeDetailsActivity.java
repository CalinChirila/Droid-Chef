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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This method determines the fragment is inflated first
        // So the fragment has no time to get data
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        mCurrentRecipe = getIntent().getParcelableExtra(MainActivity.RECIPE_PARCEL);



        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.RECIPE_PARCEL, mCurrentRecipe);

        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        recipeDetailsFragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.recipe_details_container, recipeDetailsFragment)
                .commit();

        //TODO: it works!! Now figure it out on the tablet then combine this logic with the one you have on github

        // If this layout exists, it means we deal with a tablet user
        if(findViewById(R.id.tablet_step_details_layout) != null){
            // Handle the tablet layout here
            // Create a new instance of the exo player and description fragments
            isTwoPane = true;



        } else {
            // Handle the phone layout here
            // Set an onClickListener to the views that represent a step
            // When clicked, create a new activity that launches that step's details
            isTwoPane = false;
        }

    }

}
