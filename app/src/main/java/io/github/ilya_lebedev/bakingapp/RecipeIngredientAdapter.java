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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * RecipeIngredientAdapter
 */
public class RecipeIngredientAdapter
        extends RecyclerView.Adapter<RecipeIngredientAdapter.RecipeIngredientAdapterViewHolder>{

    private Context mContext;
    private Cursor mCursor;

    public RecipeIngredientAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecipeIngredientAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ingredientView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_ingredient, parent, false);
        ingredientView.setFocusable(true);

        return new RecipeIngredientAdapterViewHolder(ingredientView);
    }

    @Override
    public void onBindViewHolder(RecipeIngredientAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String ingredientIngredient = mCursor.getString(RecipeFragment.INDEX_INGREDIENT_INGREDIENT);
        String ingredientQuantity = mCursor.getString(RecipeFragment.INDEX_INGREDIENT_QUANTITY);
        String ingredientMeasure = mCursor.getString(RecipeFragment.INDEX_INGREDIENT_MEASURE);

        holder.ingredientIngredient.setText(ingredientIngredient);
        holder.ingredientQuantityAndMeasure.setText(
                mContext.getString(R.string.format_quantity, ingredientQuantity, ingredientMeasure));
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * Swap the cursor used by RecipeIngredientAdapter for its ingredients data.
     *
     * @param cursor the new cursor to use as RecipeIngredientAdapter's data source
     */
    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    /**
     * RecipeIngredientAdapterViewHolder
     */
    class RecipeIngredientAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView ingredientIngredient;
        final TextView ingredientQuantityAndMeasure;

        public RecipeIngredientAdapterViewHolder(View itemView) {
            super(itemView);

            ingredientIngredient = itemView.findViewById(R.id.tv_ingredient);
            ingredientQuantityAndMeasure = itemView.findViewById(R.id.tv_quantity_and_measure);
        }
    }

}
