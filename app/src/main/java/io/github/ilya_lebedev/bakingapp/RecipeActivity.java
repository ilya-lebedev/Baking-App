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
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import io.github.ilya_lebedev.bakingapp.data.BakingProvider;

/**
 * RecipeActivity
 */
public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnStepClickListener {

    public static final String LOG_TAG = RecipeActivity.class.getSimpleName();

    private Uri mUri;

    private boolean mTwoPaneMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mUri = getIntent().getData();
        if (mUri == null) {
            throw new NullPointerException("URI for RecipeActivity cannot be null");
        }

        if (findViewById(R.id.recipe_linear_layout) != null) {
            mTwoPaneMode = true;

            StepFragment stepFragment = new StepFragment();
            stepFragment.setRecipeUri(null);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_container, stepFragment)
                    .commit();
        } else {
            mTwoPaneMode = false;
        }

        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setRecipeUri(mUri);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_container, recipeFragment)
                .commit();
    }

    @Override
    public void onStepSelected(int stepId) {
        Uri stepUri = BakingProvider.Step.withId(stepId);

        if (mTwoPaneMode) {
            replaceStepFragment(stepUri);
        } else {
            startStepActivity(stepUri);
        }
    }

    /**
     * Start step activity
     *
     * @param stepUri URI of the step
     */
    private void startStepActivity(Uri stepUri) {
        Intent intent = new Intent(this, StepActivity.class);
        intent.setData(stepUri);
        startActivity(intent);
    }

    /**
     * Replace step fragment
     *
     * @param stepUri URI of the step
     */
    private void replaceStepFragment(Uri stepUri) {
        StepFragment stepFragment = new StepFragment();
        stepFragment.setRecipeUri(stepUri);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();
    }

}
