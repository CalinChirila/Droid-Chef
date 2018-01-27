package com.example.android.droidchef.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.droidchef.CustomObjects.Recipe;
import com.example.android.droidchef.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Astraeus on 1/10/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private ArrayList<Recipe> mRecipeData;

    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item_layout, parent, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        Recipe currentRecipe = mRecipeData.get(position);

        String currentRecipeName = currentRecipe.getRecipeName();
        String currentRecipeImage = currentRecipe.getRecipeImage();

        if(!TextUtils.isEmpty(currentRecipeImage)) {
            Picasso.with(holder.mRecipeImage.getContext())
                    .load(currentRecipeImage)
                    .into(holder.mRecipeImage);
        } else {
            Picasso.with(holder.mRecipeImage.getContext())
                    .load(R.drawable.chefhat)
                    .into(holder.mRecipeImage);
        }

        holder.mRecipeName.setText(currentRecipeName);
    }

    @Override
    public int getItemCount() {
        if(mRecipeData == null) return 0;
        return mRecipeData.size();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mRecipeName;
        ImageView mRecipeImage;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            mRecipeName = itemView.findViewById(R.id.cv_recipe_name);
            mRecipeImage = itemView.findViewById(R.id.iv_recipe_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe recipe = mRecipeData.get(getAdapterPosition());
            mClickHandler.onClick(recipe);
        }
    }

    public void setRecipeData(ArrayList<Recipe> data){
        mRecipeData = data;
        notifyDataSetChanged();
    }
}
