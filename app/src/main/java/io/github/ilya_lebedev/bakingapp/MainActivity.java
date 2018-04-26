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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.github.ilya_lebedev.bakingapp.data.BakingContract;
import io.github.ilya_lebedev.bakingapp.data.BakingProvider;

/**
 * MainActivity
 * Main activity of the app.
 * Represents list of the recipes.
 */
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        MainRecipeAdapter.RecipeAdapterOnClickHandler {

    /*
     * The columns which is needed for displaying list of recipes within MainActivity.
     */
    public static final String[] MAIN_RECIPE_PROJECTION = {
            BakingContract.Recipe.BAKING_ID,
            BakingContract.Recipe.NAME
    };

    /*
     * This indices representing the values in the array of String above.
     * Uses for more quickly access to the data from query.
     * WARN: If the order or the contents of the Strings above changes,
     * these indices must be adjust to match the changes.
     */
    public static final int INDEX_RECIPE_BAKING_ID = 0;
    public static final int INDEX_RECIPE_NAME = 1;

    private static final int ID_RECIPE_LOADER = 91;

    private RecyclerView mRecyclerView;

    private MainRecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_recipes);

        mRecipeAdapter = new MainRecipeAdapter(this, this);

        final GridLayoutManager layoutManager = new GridLayoutManager(
                this,
                getResources().getInteger(R.integer.recipe_grid_span_count));

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mRecipeAdapter);

        getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case ID_RECIPE_LOADER: {
                Uri uri = BakingProvider.Recipe.RECIPE;

                return new CursorLoader(this,
                        uri,
                        MAIN_RECIPE_PROJECTION,
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
        if (cursor == null) {
            return;
        }

        mRecipeAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecipeAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int recipeBakingId) {
        Intent intent = new Intent(this, RecipeActivity.class);
        Uri uri = BakingProvider.Recipe.withRecipeBakingId(recipeBakingId);
        intent.setData(uri);
        startActivity(intent);
    }

}
