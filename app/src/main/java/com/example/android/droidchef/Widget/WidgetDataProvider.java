package com.example.android.droidchef.Widget;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.droidchef.R;
import com.example.android.droidchef.Widget.WidgetData.RecipeWidgetContract;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Astraeus on 1/22/2018.
 * This is the FACTORY
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private String mIngredientsListString;
    private ArrayList<String> mIngredientsList;
    private Context mContext;

    private int mRecipeNumber = 0;
    private Cursor mCursor;

    public WidgetDataProvider(Context context){
        mContext = context;

        String[] projection = new String[]{RecipeWidgetContract.RecipeEntry._ID, RecipeWidgetContract.RecipeEntry.COLUMN_INGREDIENTS};
        String selection = RecipeWidgetContract.RecipeEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(mRecipeNumber)};

        mCursor = mContext.getContentResolver().query(RecipeWidgetContract.RecipeEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        mCursor.moveToPosition(mRecipeNumber);
    }

    @Override
    public void onCreate() {
        Log.v("IMPORTANT", "Crash test");
    }


    @Override
    public void onDataSetChanged() {

        // Update mIngredientsListString with the correct information
        mRecipeNumber = RecipeNumberService.mRecipeNumber;

        mCursor.moveToPosition(mRecipeNumber);
        mIngredientsListString = mCursor.getString(mCursor.getColumnIndex(RecipeWidgetContract.RecipeEntry.COLUMN_INGREDIENTS));

        mIngredientsList = new ArrayList<>(Arrays.asList(mIngredientsListString.split("\n")));

    }

    @Override
    public void onDestroy() {
        if(mCursor != null){
            mCursor.close();
            mCursor = null;
        }
    }

    @Override
    public int getCount() {
        return mIngredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        // This return 1 widget list item
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        String ingredient = mIngredientsList.get(position);
        rv.setTextViewText(R.id.tv_widget_list_item, ingredient);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
