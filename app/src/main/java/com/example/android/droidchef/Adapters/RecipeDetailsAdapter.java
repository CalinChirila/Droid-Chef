package com.example.android.droidchef.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.droidchef.CustomObjects.Ingredient;
import com.example.android.droidchef.CustomObjects.Recipe;
import com.example.android.droidchef.CustomObjects.Step;
import com.example.android.droidchef.R;

import java.util.ArrayList;

/**
 * Created by Astraeus on 1/12/2018.
 */

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeDetailsViewHolder> {

    private Recipe mRecipeData;

    private static final String PARCELABLE_STEP = "step";

    private final RecipeDetailsClickHandler mClickHandler;

    public interface RecipeDetailsClickHandler {
        void onClick(Step step);
    }

    public RecipeDetailsAdapter(Recipe data, RecipeDetailsClickHandler handler){
        mRecipeData = data;
        mClickHandler = handler;
    }


    @Override
    public RecipeDetailsAdapter.RecipeDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_details_list_item_layout, parent, false);
        return new RecipeDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeDetailsAdapter.RecipeDetailsViewHolder holder, int position) {
        if(position == 0){
            holder.mRecipeDetailTextView.setText(buildIngredientsText(mRecipeData.getRecipeIngredients()));
        } else {
            holder.mRecipeDetailTextView.setText(mRecipeData.getRecipeSteps().get(position - 1).getStepShortDescription());
        }



    }

    @Override
    public int getItemCount() {
        if(mRecipeData == null) return 0;
        return mRecipeData.getRecipeSteps().size() + 1;
    }


    public class RecipeDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mRecipeDetailTextView;

        public RecipeDetailsViewHolder(View itemView) {
            super(itemView);

            mRecipeDetailTextView = itemView.findViewById(R.id.recipe_details_text_view);
            if(getAdapterPosition() != 0){
                itemView.setOnClickListener(this);
            }


        }

        @Override
        public void onClick(View view) {
            Step step = mRecipeData.getRecipeSteps().get(getAdapterPosition() - 1);
            mClickHandler.onClick(step);
        }

    }

    /**
     * Build the list of ingredients as a String so that it can be displayed in the first
     * layout of the recipe details screen
     * @param ingredientsList = the list of ingredients from the current recipe
     * @return details for every ingredient on each line
     */
    private String buildIngredientsText(ArrayList<Ingredient> ingredientsList){

        StringBuilder builder = new StringBuilder();
        for(Ingredient ingredient : ingredientsList){
            String ingredientName = ingredient.getIngredientName();
            String ingredientQuantity = String.valueOf(ingredient.getIngredientQuantity());
            String ingredientMeasure = ingredient.getIngredientMeasure();
            builder.append(ingredientName + " - " + ingredientQuantity + " - " + ingredientMeasure + "\n");
        }

        return builder.toString();
    }

}
