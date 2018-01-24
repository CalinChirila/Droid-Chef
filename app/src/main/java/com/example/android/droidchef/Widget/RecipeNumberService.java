package com.example.android.droidchef.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Astraeus on 1/23/2018.
 */

public class RecipeNumberService extends IntentService {

    public static int mRecipeNumber = 0;
    private int numberOfRecipes;

    public RecipeNumberService() {
        super("recipeNumberService");
    }

    public static int getCurrentRecipeNumber(){ return mRecipeNumber; }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;
        Context context = getApplicationContext();

        numberOfRecipes = intent.getIntExtra(RecipeWidget.EXTRA_NUMBER_OF_RECIPES, 0);

        String action = intent.getAction();
        if(action.equals(RecipeWidget.ACTION_NEXT_RECIPE) && mRecipeNumber <= numberOfRecipes - 2){
            mRecipeNumber++;
        }

        if(action.equals(RecipeWidget.ACTION_PREVIOUS_RECIPE) && mRecipeNumber > 0){
            mRecipeNumber--;
        }


        Intent updateWidgetIntent = new Intent(this, RecipeWidget.class);
        updateWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, RecipeWidget.class));
        updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);

        sendBroadcast(updateWidgetIntent);
    }
}
