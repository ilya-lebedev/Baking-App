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
package io.github.ilya_lebedev.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import io.github.ilya_lebedev.bakingapp.R;
import io.github.ilya_lebedev.bakingapp.data.BakingContract;
import io.github.ilya_lebedev.bakingapp.data.BakingPreferences;
import io.github.ilya_lebedev.bakingapp.data.BakingProvider;

/**
 * ListWidgetService
 */
public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

}

/**
 * ListRemoteViewsFactory
 */
class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    /*
     * The columns which is needed for displaying list of ingredients within RecipeFragment.
     */
    public static final String[] RECIPE_INGREDIENT_PROJECTION = {
            BakingContract.Ingredient.INGREDIENT,
            BakingContract.Ingredient.QUANTITY,
            BakingContract.Ingredient.MEASURE
    };

    /*
     * This indices representing the values in the array of String above.
     * Uses for more quickly access to the data from query.
     * WARN: If the order or the contents of the Strings above changes,
     * these indices must be adjust to match the changes.
     */
    public static final int INDEX_INGREDIENT_INGREDIENT = 0;
    public static final int INDEX_INGREDIENT_QUANTITY = 1;
    public static final int INDEX_INGREDIENT_MEASURE = 2;

    Context mContext;
    Cursor mCursor;
    private Uri mIngredientsUri;

    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        int desiredRecipeId = BakingPreferences.getDesiredRecipe(mContext);
        int desiredRecipeDefaultId = mContext.getResources()
                .getInteger(R.integer.pref_desired_recipe_default);

        if (desiredRecipeId == desiredRecipeDefaultId) {
            mCursor = null;
            return;
        }

        mIngredientsUri = BakingProvider.Ingredient.withRecipeBakingId(desiredRecipeId);

        if (mCursor != null) {
            mCursor.close();
        }

        // Revert back to our process' identity so we can work with our
        // content provider
        final long identityToken = Binder.clearCallingIdentity();
        mCursor = mContext.getContentResolver().query(
                mIngredientsUri,
                RECIPE_INGREDIENT_PROJECTION,
                null,
                null,
                BakingContract.Ingredient.INGREDIENT);
        // Restore the identity
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (mCursor == null || mCursor.getCount() == 0) {
            return null;
        }

        mCursor.moveToPosition(position);

        String ingredientIngredient = mCursor.getString(INDEX_INGREDIENT_INGREDIENT);
        String ingredientQuantity = mCursor.getString(INDEX_INGREDIENT_QUANTITY);
        String ingredientMeasure = mCursor.getString(INDEX_INGREDIENT_MEASURE);
        String quantityMeasure = mContext.getString(R.string.format_quantity,
                ingredientQuantity, ingredientMeasure);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_item_baking_widget);

        views.setTextViewText(R.id.tv_ingredient, ingredientIngredient);
        views.setTextViewText(R.id.tv_quantity_and_measure, quantityMeasure);

        Intent fillInIntent = new Intent();
        fillInIntent.setData(mIngredientsUri);
        views.setOnClickFillInIntent(R.id.baking_widget_list_item, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        // Treat all items in the GridView the same
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
