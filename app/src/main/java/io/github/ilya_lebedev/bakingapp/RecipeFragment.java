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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.ilya_lebedev.bakingapp.data.BakingContract;

/**
 * RecipeFragment
 */
public class RecipeFragment extends Fragment
        implements RecipeStepAdapter.RecipeStepAdapterOnClickHandler {

    /*
     * The columns which is needed for displaying list of steps within RecipeFragment.
     */
    public static final String[] RECIPE_STEP_PROJECTION = {
            BakingContract.Step.BAKING_ID,
            BakingContract.Step.SHORT_DESCRIPTION
    };

    /*
     * This indices representing the values in the array of String above.
     * Uses for more quickly access to the data from query.
     * WARN: If the order or the contents of the Strings above changes,
     * these indices must be adjust to match the changes.
     */
    public static final int INDEX_STEP_BAKING_ID = 0;
    public static final int INDEX_STEP_SHORT_DESCRIPTION = 1;

    // Mandatory empty constructor
    public RecipeFragment() {
    }

    // Inflates the view of recipe
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        return rootView;
    }

    @Override
    public void onClick(int stepBakingId) {
        // TODO
    }
}
