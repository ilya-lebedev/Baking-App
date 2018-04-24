/*
 * Copyright (C) 2018 Ilya Lebedev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ilya_lebedev.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link MainRecipeAdapter} exposes a list of recipes
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}
 */
public class MainRecipeAdapter extends RecyclerView.Adapter<MainRecipeAdapter.RecipeAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    final private RecipeAdapterOnClickHandler mClickHandler;

    public MainRecipeAdapter(@NonNull Context context, RecipeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_recipe, parent, false);
        view.setFocusable(true);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String recipeName = mCursor.getString(MainActivity.INDEX_RECIPE_NAME);

        holder.recipeNameTv.setText(recipeName);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * Swap the cursor used by MainRecipeAdapter for its recipes data.
     *
     * @param cursor the new cursor to use as MainRecipeAdapter's data source
     */
    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    /**
     * RecipeAdapterOnClickHandler
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(int recipeBakingId);
    }

    /**
     * RecipeAdapterViewHolder
     */
    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView recipeNameTv;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            recipeNameTv = itemView.findViewById(R.id.tv_recipe_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int recipeBakingId = mCursor.getInt(MainActivity.INDEX_RECIPE_BAKING_ID);
            mClickHandler.onClick(recipeBakingId);
        }
    }

}
