package com.example.android.droidchef.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.droidchef.CustomObjects.Ingredient;
import com.example.android.droidchef.CustomObjects.Recipe;
import com.example.android.droidchef.CustomObjects.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Astraeus on 1/10/2018.
 */

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();

    // Parse the JSON response from the network call and return a list of Recipe objects
    public static ArrayList<Recipe> parseRecipesJson(String jsonResponse) {
        // If the provided JSON String is empty, exit early
        if (jsonResponse == null || TextUtils.isEmpty(jsonResponse)) return null;

        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<Ingredient> ingredients;
        ArrayList<Step> steps;

        try {
            JSONArray rootArray = new JSONArray(jsonResponse);
            for (int i = 0; i < rootArray.length(); i++) {
                // Get the recipe at the current index position
                JSONObject recipeObject = rootArray.getJSONObject(i);
                // Get the recipe name
                String recipeName = recipeObject.optString("name");
                JSONArray ingredientsArray = recipeObject.optJSONArray("ingredients");
                ingredients = getIngredientsForRecipe(ingredientsArray);
                JSONArray stepsArray = recipeObject.optJSONArray("steps");
                steps = getStepsForRecipe(stepsArray);
                String recipeImage = recipeObject.optString("image");

                recipes.add(new Recipe(recipeName, ingredients, steps, recipeImage));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Couldn't parse the provided JSON string");
        }

        return recipes;
    }

    /**
     * Helper method to extract the list of ingredients for a recipe
     */
    private static ArrayList<Ingredient> getIngredientsForRecipe(JSONArray ingredientsArray) {
        // If the provided JSONArray is null or empty, return null.
        if(ingredientsArray == null || ingredientsArray.length() == 0) return null;

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try {
            for (int i = 0; i < ingredientsArray.length(); i++) {
                JSONObject currentIngredient = ingredientsArray.getJSONObject(i);
                String ingredientName = currentIngredient.optString("ingredient");
                double ingredientQuantity = currentIngredient.optDouble("quantity");
                String ingredientMeasure = currentIngredient.optString("measure");

                ingredients.add(new Ingredient(ingredientName, ingredientQuantity, ingredientMeasure));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    /**
     * Helper method to extract the steps for a recipe
     */
    private static ArrayList<Step> getStepsForRecipe(JSONArray stepsArray) {
        //If the provided JSONArray is null or empty, return null.
        if(stepsArray == null || stepsArray.length() == 0) return null;

        ArrayList<Step> steps = new ArrayList<>();
        try {
            for (int i = 0; i < stepsArray.length(); i++) {
                JSONObject currentStep = stepsArray.getJSONObject(i);
                int stepID = currentStep.optInt("id");
                String shortStepDescription = currentStep.optString("shortDescription");
                String stepDescription = currentStep.optString("description");
                String videoURLString = currentStep.optString("videoURL");
                String thumbnailURLString = currentStep.optString("thumbnailURL");

                steps.add(new Step(stepID, shortStepDescription, stepDescription, videoURLString, thumbnailURLString));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return steps;
    }
}
