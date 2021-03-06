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

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.ilya_lebedev.bakingapp.data.BakingContract;
import io.github.ilya_lebedev.bakingapp.data.BakingProvider;

/**
 * RecipeFragment
 */
public class RecipeFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        RecipeStepAdapter.RecipeStepAdapterOnClickHandler {

    public static final String LOG_TAG = RecipeFragment.class.getSimpleName();

    /*
     * The columns which is needed for displaying recipe details within RecipeFragment.
     */
    public static final String[] RECIPE_PROJECTION = {
            BakingContract.Recipe.NAME
    };

    /*
     * This indices representing the values in the array of String above.
     * Uses for more quickly access to the data from query.
     * WARN: If the order or the contents of the Strings above changes,
     * these indices must be adjust to match the changes.
     */
    public static final int INDEX_RECIPE_NAME = 0;

    /*
     * The columns which is needed for displaying list of steps within RecipeFragment.
     */
    public static final String[] RECIPE_STEP_PROJECTION = {
            BakingContract.Step._ID,
            BakingContract.Step.SHORT_DESCRIPTION
    };

    /*
     * This indices representing the values in the array of String above.
     * Uses for more quickly access to the data from query.
     * WARN: If the order or the contents of the Strings above changes,
     * these indices must be adjust to match the changes.
     */
    public static final int INDEX_STEP_ID = 0;
    public static final int INDEX_STEP_SHORT_DESCRIPTION = 1;

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

    private static final int ID_RECIPE_LOADER = 77;
    private static final int ID_STEP_LOADER = 78;
    private static final int ID_INGREDIENT_LOADER = 79;

    private static final String KEY_ADAPTER_SELECTED_POSITION = "adapter_selected_position";

    private TextView mRecipeNameTv;

    private RecyclerView mRecyclerView;
    private RecyclerView mIngredientRecyclerView;

    private RecipeStepAdapter mStepAdapter;
    private RecipeIngredientAdapter mIngredientAdapter;

    private int mAdapterSelectedPosition = -1;

    private Uri mRecipeUri;

    // Define a new interface OnStepClickListener that triggers a callback in the host activity
    private OnStepClickListener mOnStepClickListener;

    // Mandatory empty constructor
    public RecipeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnStepClickListener = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    // Inflates the view of recipe
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecipeNameTv = rootView.findViewById(R.id.tv_recipe_name);

        mRecyclerView = rootView.findViewById(R.id.rv_steps);
        mStepAdapter = new RecipeStepAdapter(getContext(), this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setAdapter(mStepAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        mIngredientRecyclerView = rootView.findViewById(R.id.rv_ingredients);
        mIngredientAdapter = new RecipeIngredientAdapter(getContext());
        final LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(getContext());

        mIngredientRecyclerView.setAdapter(mIngredientAdapter);
        mIngredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        mIngredientRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                ingredientLayoutManager.getOrientation()));
        mIngredientRecyclerView.setHasFixedSize(true);
        mIngredientRecyclerView.setNestedScrollingEnabled(false);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(KEY_ADAPTER_SELECTED_POSITION)) {
            mAdapterSelectedPosition = savedInstanceState.getInt(KEY_ADAPTER_SELECTED_POSITION);
            mStepAdapter.setPosition(mAdapterSelectedPosition);
        }

        getLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
        getLoaderManager().initLoader(ID_STEP_LOADER, null, this);
        getLoaderManager().initLoader(ID_INGREDIENT_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_ADAPTER_SELECTED_POSITION, mAdapterSelectedPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case ID_RECIPE_LOADER: {
                return new CursorLoader(getContext(),
                        mRecipeUri,
                        RECIPE_PROJECTION,
                        null,
                        null,
                        null);
            }

            case ID_STEP_LOADER: {
                long recipeBakingId = ContentUris.parseId(mRecipeUri);
                Uri uri = BakingProvider.Step.withRecipeBakingId(recipeBakingId);

                return new CursorLoader(getContext(),
                        uri,
                        RECIPE_STEP_PROJECTION,
                        null,
                        null,
                        BakingContract.Step.BAKING_ID + " ASC");
            }

            case ID_INGREDIENT_LOADER: {
                long recipeBakingId = ContentUris.parseId(mRecipeUri);
                Uri uri = BakingProvider.Ingredient.withRecipeBakingId(recipeBakingId);

                return new CursorLoader(getContext(),
                        uri,
                        RECIPE_INGREDIENT_PROJECTION,
                        null,
                        null,
                        null);
            }

            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);

        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int loaderId = loader.getId();

        switch (loaderId) {

            case ID_RECIPE_LOADER:
                fillRecipeView(cursor);
                break;

            case ID_STEP_LOADER:
                mStepAdapter.swapCursor(cursor);
                break;

            case ID_INGREDIENT_LOADER:
                mIngredientAdapter.swapCursor(cursor);
                break;

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mStepAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int stepId, int adapterSelectedPosition) {
        mOnStepClickListener.onStepSelected(stepId);
        mAdapterSelectedPosition = adapterSelectedPosition;
    }

    public void setRecipeUri(Uri recipeUri) {
        mRecipeUri = recipeUri;
    }

    // OnStepClickListener interface, calls a method in the host activity named onStepSelected
    public interface OnStepClickListener {
        void onStepSelected(int stepId);
    }

    /**
     * Fill in recipe view
     *
     * @param cursor contain recipe data
     */
    private void fillRecipeView(Cursor cursor) {
        if (cursor == null) {
            return;
        }

        if (cursor.moveToFirst()) {
            String recipeName = cursor.getString(INDEX_RECIPE_NAME);
            mRecipeNameTv.setText(recipeName);
        }
    }

}
