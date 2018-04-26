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
package io.github.ilya_lebedev.bakingapp.utilities;

import android.content.ContentValues;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.github.ilya_lebedev.bakingapp.data.BakingContract;
import io.github.ilya_lebedev.bakingapp.data.BakingData;

/**
 * BakingJsonUtils
 */
public class BakingJsonUtils {

    /* JSON keys of recipe object */
    private static final String RECIPE_BAKING_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_STEPS = "steps";

    /* JSON keys of step object */
    private static final String STEP_BAKING_ID = "id";
    private static final String STEP_SHORT_DESCRIPTION = "shortDescription";
    private static final String STEP_DESCRIPTION = "description";
    private static final String STEP_VIDEO_URL = "videoUrl";
    private static final String STEP_THUMBNAIL_URL = "thumbnailUrl";

    /* JSON keys of ingredient object */
    private static final String INGREDIENT_QUANTITY = "quantity";
    private static final String INGREDIENT_MEASURE = "measure";
    private static final String INGREDIENT_INGREDIENT = "ingredient";

    /**
     * Get baking data from JSON
     *
     * @param bakingJsonString baking data JSON string
     *
     * @return set of baking data content values arrays
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static BakingData getBakingDataFromJson(String bakingJsonString) throws JSONException {

        List<ContentValues> recipesContentValues = new ArrayList<>();
        List<ContentValues> stepsContentValues = new ArrayList<>();
        List<ContentValues> ingredientsContentValues = new ArrayList<>();

        JSONArray bakingJsonArray = new JSONArray(bakingJsonString);

        /* Parse recipes with ingredients and steps */
        for (int i = 0; i < bakingJsonArray.length(); i++) {
            JSONObject recipeJsonObject = bakingJsonArray.getJSONObject(i);

            int recipeBakingId = recipeJsonObject.getInt(RECIPE_BAKING_ID);
            String recipeName = recipeJsonObject.getString(RECIPE_NAME);

            ContentValues recipe = new ContentValues();
            recipe.put(BakingContract.Recipe.BAKING_ID, recipeBakingId);
            recipe.put(BakingContract.Recipe.NAME, recipeName);

            recipesContentValues.add(recipe);

            /* Parse steps for current recipe */
            JSONArray stepsJsonArray = recipeJsonObject.getJSONArray(RECIPE_STEPS);
            for (int j = 0; j < stepsJsonArray.length(); j++) {
                JSONObject stepJsonObject = stepsJsonArray.getJSONObject(j);

                int stepBakingId = stepJsonObject.getInt(STEP_BAKING_ID);
                String stepShortDescription = stepJsonObject.getString(STEP_SHORT_DESCRIPTION);
                String stepDescription = stepJsonObject.getString(STEP_DESCRIPTION);
                String stepVideoUrl = stepJsonObject.getString(STEP_VIDEO_URL);
                if (TextUtils.isEmpty(stepVideoUrl)) {
                    stepVideoUrl = null;
                }
                String stepThumbnailUrl = stepJsonObject.getString(STEP_THUMBNAIL_URL);
                if (TextUtils.isEmpty(stepThumbnailUrl)) {
                    stepThumbnailUrl = null;
                }

                ContentValues step = new ContentValues();
                step.put(BakingContract.Step.BAKING_ID, stepBakingId);
                step.put(BakingContract.Step.SHORT_DESCRIPTION, stepShortDescription);
                step.put(BakingContract.Step.DESCRIPTION, stepDescription);
                step.put(BakingContract.Step.VIDEO_URL, stepVideoUrl);
                step.put(BakingContract.Step.THUMBNAIL_URL, stepThumbnailUrl);
                step.put(BakingContract.Step.BAKING_RECIPE_ID, recipeBakingId);

                stepsContentValues.add(step);
            }

            /* Parse ingredients for current recipe */
            JSONArray ingredientsJsonArray = recipeJsonObject.getJSONArray(RECIPE_INGREDIENTS);
            for (int k = 0; k < ingredientsJsonArray.length(); k++) {
                JSONObject ingredientJsonObject = ingredientsJsonArray.getJSONObject(k);

                String ingredientIngredient = ingredientJsonObject.getString(INGREDIENT_INGREDIENT);
                String ingredientQuantity = ingredientJsonObject.getString(INGREDIENT_QUANTITY);
                String ingredientMeasure = ingredientJsonObject.getString(INGREDIENT_MEASURE);

                ContentValues ingredient = new ContentValues();
                ingredient.put(BakingContract.Ingredient.INGREDIENT, ingredientIngredient);
                ingredient.put(BakingContract.Ingredient.QUANTITY, ingredientQuantity);
                ingredient.put(BakingContract.Ingredient.MEASURE, ingredientMeasure);
                ingredient.put(BakingContract.Ingredient.BAKING_RECIPE_ID, recipeBakingId);

                ingredientsContentValues.add(ingredient);
            }
        }

        BakingData bakingData = new BakingData();

        bakingData.recipesContentValues = recipesContentValues
                .toArray(new ContentValues[recipesContentValues.size()]);
        bakingData.stepsContentValues = stepsContentValues
                .toArray(new ContentValues[stepsContentValues.size()]);
        bakingData.ingredientsContentValues = ingredientsContentValues
                .toArray(new ContentValues[ingredientsContentValues.size()]);

        return bakingData;
    }

}
