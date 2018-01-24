package com.example.android.droidchef.Widget.WidgetData;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Astraeus on 1/22/2018.
 */

public class RecipeWidgetContract {
    private static final String AUTHORITY = "com.example.android.droidchef.Widget.WidgetData";
    private static Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPE = "recipes";

    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                        .buildUpon()
                        .appendPath(PATH_RECIPE)
                        .build();

        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_RECIPE_NAME = "recipeName";
        public static final String COLUMN_INGREDIENTS = "ingredientsList";
    }
}
