package com.example.android.droidchef.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.droidchef.CustomObjects.Recipe;
import com.example.android.droidchef.CustomObjects.Step;
import com.example.android.droidchef.R;
import com.example.android.droidchef.Adapters.RecipeDetailsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsAdapter.RecipeDetailsAdapterOnClickHandler {
    @BindView(R.id.rv_recipe_details)
    RecyclerView mRecipeDetailsRecyclerView;

    private RecipeDetailsAdapter mDetailsAdapter;
    private Recipe mSelectedRecipe;
    public static final String STEP_PARCEL = "stepParcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        mSelectedRecipe = getIntent().getParcelableExtra(MainActivity.RECIPE_PARCEL);
        mDetailsAdapter = new RecipeDetailsAdapter(this);

        mDetailsAdapter.setRecipeDetailsData(mSelectedRecipe);

        LinearLayoutManager recipeDetailsLayoutManager = new LinearLayoutManager(this);
        recipeDetailsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecipeDetailsRecyclerView.setHasFixedSize(true);
        mRecipeDetailsRecyclerView.setLayoutManager(recipeDetailsLayoutManager);
        mRecipeDetailsRecyclerView.setAdapter(mDetailsAdapter);

    }

    @Override
    public void onClick(Step recipeStep) {
        Intent intent = new Intent(RecipeDetailsActivity.this, StepDetailsActivity.class);
        intent.putExtra(STEP_PARCEL, recipeStep);
        intent.putExtra(MainActivity.RECIPE_PARCEL, mSelectedRecipe);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
}
