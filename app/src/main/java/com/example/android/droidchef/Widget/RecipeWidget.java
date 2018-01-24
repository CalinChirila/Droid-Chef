package com.example.android.droidchef.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.example.android.droidchef.R;
import com.example.android.droidchef.Widget.WidgetData.RecipeWidgetContract;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static int recipeNumber;
    private static RemoteViews views;

    public static final String EXTRA_INGREDIENTS_STRING = "ingredientsString";
    public static final String EXTRA_INGREDIENTS_LIST_SIZE = "ingredientsSize";
    public static final String EXTRA_NUMBER_OF_RECIPES = "numberOfRecipes";

    public static final String ACTION_NEXT_RECIPE = "com.example.android.droidchef.nextRecipe";
    public static final String ACTION_PREVIOUS_RECIPE = "com.example.android.droidchef.previousRecipe";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Set the buttons click listeners here
        // Construct the RemoteViews object
        // This is the entire widget layout. Set the recipe name here
        views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        recipeNumber = RecipeNumberService.getCurrentRecipeNumber();

        Cursor mCursor = context.getContentResolver().query(RecipeWidgetContract.RecipeEntry.CONTENT_URI,
                null,
                RecipeWidgetContract.RecipeEntry._ID + "=?",
                new String[]{String.valueOf(recipeNumber)},
                null);

        int numberOfRecipes = mCursor.getCount();

        if(mCursor.moveToPosition(recipeNumber)) {

            String recipeName = mCursor.getString(mCursor.getColumnIndex(RecipeWidgetContract.RecipeEntry.COLUMN_RECIPE_NAME));
            String ingredientsListString = mCursor.getString(mCursor.getColumnIndex(RecipeWidgetContract.RecipeEntry.COLUMN_INGREDIENTS));


            views.setTextViewText(R.id.tv_widget_recipe_name, recipeName);

            // The first intent
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(EXTRA_INGREDIENTS_STRING, ingredientsListString);
            views.setRemoteAdapter(R.id.widget_ingredients_list, intent);





            // Set the intent for the next recipe
            Intent nextRecipeIntent = new Intent(context, RecipeNumberService.class);
            // In this intent, put the action and the size of the ingredientsList
            nextRecipeIntent.setAction(ACTION_NEXT_RECIPE);
            nextRecipeIntent.putExtra(EXTRA_NUMBER_OF_RECIPES, numberOfRecipes);



            PendingIntent nextRecipePendingIntent = PendingIntent.getService(context, 0, nextRecipeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_next_recipe_button, nextRecipePendingIntent);

            // Set the intent fot the previous recipe
            Intent previousRecipeIntent = new Intent(context, RecipeNumberService.class);
            previousRecipeIntent.setAction(ACTION_PREVIOUS_RECIPE);


            PendingIntent previousRecipePendingIntent = PendingIntent.getService(context, 0, previousRecipeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_previous_recipe_button, previousRecipePendingIntent);

            // Instruct the widget manager to update the widget

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

            int[] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidget.class));
            widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widget_ingredients_list);

        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

