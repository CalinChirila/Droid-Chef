package com.example.android.droidchef.Activities;


import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.droidchef.Adapters.RecipeAdapter;
import com.example.android.droidchef.CustomObjects.Ingredient;
import com.example.android.droidchef.CustomObjects.Recipe;
import com.example.android.droidchef.R;
import com.example.android.droidchef.SimpleIdlingResource;
import com.example.android.droidchef.Utils.JSONUtils;
import com.example.android.droidchef.Utils.NetworkUtils;
import com.example.android.droidchef.Widget.WidgetData.RecipeWidgetContract;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Recipe>>{

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String RECIPE_PARCEL = "recipeParcel";

    @Nullable public static SimpleIdlingResource mIdlingResource;

    @BindView(R.id.rv_recipe_list)
    RecyclerView mRecipesRecyclerView;
    @BindView(R.id.tv_recipe_empty_state)
    TextView mEmptyState;

    public static RecipeAdapter mRecipeAdapter;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Check internet connection
        ConnectivityManager cm =(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork;
        if(cm != null){
            activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        if(isConnected) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            mRecipeAdapter = new RecipeAdapter(this);

            mRecipesRecyclerView.setLayoutManager(layoutManager);
            mRecipesRecyclerView.setHasFixedSize(true);
            mRecipesRecyclerView.setAdapter(mRecipeAdapter);

            getLoaderManager().initLoader(1, null, this);
        } else {
            setEmptyStateWithText(getString(R.string.no_network_message));
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        // Start the RecipeDetails activity
        // In the intent, put the provided recipe as an extra parcelable
        Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
        intent.putExtra(RECIPE_PARCEL, recipe);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(getApplicationContext()) {

            @Override
            public void onStartLoading(){
                if(mIdlingResource != null) {
                    mIdlingResource.setIdleState(false);
                }
                forceLoad();
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                ArrayList<Recipe> recipes = null;
                try{
                    String jsonResponse = NetworkUtils.makeHttpRequest(NetworkUtils.buildURL());
                    recipes = JSONUtils.parseRecipesJson(jsonResponse);
                }catch(IOException e){
                    Log.e(TAG, "Encountered a problem with the network request");
                }
                return recipes;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> recipes) {
        if(recipes != null) {
            mRecipeAdapter.setRecipeData(recipes);
            loadRecipesIntoWidgetDatabase(recipes);
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
        } else {
            setEmptyStateWithText(getString(R.string.no_recipes_message));
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {
        mRecipeAdapter.setRecipeData(null);
    }

    private void loadRecipesIntoWidgetDatabase(ArrayList<Recipe> recipes){
        // We need 2 String to put into ContentValues, 1 for current recipe name and 1 for the list of ingredients
        Cursor cursor = getContentResolver().query(RecipeWidgetContract.RecipeEntry.CONTENT_URI, null, null, null, null);
        if(cursor.getCount() != 0){
            cursor.close();
            return;
        }

        for(Recipe currentRecipe : recipes){
            String recipeName = currentRecipe.getRecipeName();

            StringBuilder ingredientsStringBuilder = new StringBuilder();
            ArrayList<Ingredient> currentIngredients = currentRecipe.getRecipeIngredients();

            for(Ingredient ingredient : currentIngredients){
                String ingredientName = ingredient.getIngredientName();
                ingredientsStringBuilder.append(ingredientName + "\n");
            }

            String ingredientsList = ingredientsStringBuilder.toString();

            ContentValues values = new ContentValues();
            values.put(RecipeWidgetContract.RecipeEntry.COLUMN_RECIPE_NAME, recipeName);
            values.put(RecipeWidgetContract.RecipeEntry.COLUMN_INGREDIENTS, ingredientsList);

            getContentResolver().insert(RecipeWidgetContract.RecipeEntry.CONTENT_URI, values);
        }
    }

    private void setEmptyStateWithText(String text){
        int visibility = mEmptyState.getVisibility();
        switch(visibility){
            case View.GONE:
            case View.INVISIBLE:
                mRecipesRecyclerView.setVisibility(View.GONE);
                mEmptyState.setVisibility(View.VISIBLE);
                mEmptyState.setText(text);
                break;
        }
    }

    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource(){
        if(mIdlingResource == null){
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
