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
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.github.ilya_lebedev.bakingapp.data.BakingContract;
import io.github.ilya_lebedev.bakingapp.data.BakingProvider;

/**
 * FakeBakingDataUtils
 */
public class FakeBakingDataUtils {

    private static final int[] recipeBakingIds = {1, 2, 3, 4};
    private static final String[] recipeNames = {"Nutella Pie", "Brownies", "Yellow Cake", "Cheesecake"};
    private static final int[][] recipesStepsIds = {
            {0, 1, 2},
            {0, 1, 2},
            {0, 1, 2},
            {0, 1, 2}
    };
    private static final String[][] recipesStepsShortDesc = {
            {"Recipe Introduction", "Starting prep", "Prep the cookie crust."},
            {"Recipe Introduction", "Starting prep", "Melt butter and bittersweet chocolate."},
            {"Recipe Introduction", "Starting prep", "Combine dry ingredients."},
            {"Recipe Introduction", "Starting prep", "Prep the cookie crust."}
    };
    private static final String[][] recipesStepsVideos = {
            {"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", null, "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4"},
            {"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc33_-intro-brownies/-intro-brownies.mp4", null, null},
            {null, null, null},
            {null, null, "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdb1d_2-form-crust-to-bottom-of-pan-cheesecake/2-form-crust-to-bottom-of-pan-cheesecake.mp4"}
    };
    private static final String[][] recipesStepsThumbnails = {
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null}
    };
    private static final String[][] recipesIngredientsIngredient = {
            {"Graham Cracker crumbs", "unsalted butter, melted", "granulated sugar", "salt"},
            {"Bittersweet chocolate (60-70% cacao)", "unsalted butter", "granulated sugar"},
            {"sifted cake flour", "granulated sugar", "baking powder"},
            {"Graham Cracker crumbs", "unsalted butter, melted", "granulated sugar"}
    };
    private static final String[][] recipesIngredientsQuantity = {
            {"2", "6", "0.5", "1.5"},
            {"350", "226", "300"},
            {"400", "700", "4"},
            {"2", "6", "250"}
    };
    private static final String[][] recipesIngredientsMeasure = {
            {"CUP", "TBLSP", "CUP", "TBLSP"},
            {"G", "G", "G"},
            {"G", "G", "TSP"},
            {"CUP", "TBLSP", "G"}
    };

    private static ContentValues createTestRecipeContentValues(int id, String name) {
        ContentValues testRecipeContentValues = new ContentValues();

        testRecipeContentValues.put(BakingContract.Recipe.BAKING_ID, id);
        testRecipeContentValues.put(BakingContract.Recipe.NAME, name);

        return testRecipeContentValues;
    }

    private static ContentValues createTestIngredientContentValues(
            String ingredient, String quantity, String measure, int recipeId) {
        ContentValues testIngredientContentValues = new ContentValues();

        testIngredientContentValues.put(BakingContract.Ingredient.INGREDIENT, ingredient);
        testIngredientContentValues.put(BakingContract.Ingredient.QUANTITY, quantity);
        testIngredientContentValues.put(BakingContract.Ingredient.MEASURE, measure);
        testIngredientContentValues.put(BakingContract.Ingredient.BAKING_RECIPE_ID, recipeId);

        return testIngredientContentValues;
    }

    private static ContentValues createTestStepContentValues(int id, String shortDesc,
                 String videoUrl, String thumbnailUrl, int recipeId) {
        ContentValues testStepContentValues = new ContentValues();

        testStepContentValues.put(BakingContract.Step.BAKING_ID, id);
        testStepContentValues.put(BakingContract.Step.SHORT_DESCRIPTION, shortDesc);
        testStepContentValues.put(BakingContract.Step.DESCRIPTION, shortDesc);
        testStepContentValues.put(BakingContract.Step.VIDEO_URL, videoUrl);
        testStepContentValues.put(BakingContract.Step.THUMBNAIL_URL, thumbnailUrl);
        testStepContentValues.put(BakingContract.Step.BAKING_RECIPE_ID, recipeId);

        return testStepContentValues;
    }

    public static void insertFakeBakingData(Context context) {
        List<ContentValues> recipeFakeValues = new ArrayList<>();
        List<ContentValues> ingredientFakeValues = new ArrayList<>();
        List<ContentValues> stepFakeValues = new ArrayList<>();

        for (int i = 0; i < recipeBakingIds.length; i++) {
            recipeFakeValues.add(
                    createTestRecipeContentValues(recipeBakingIds[i], recipeNames[i]));

            for (int j = 0; j < recipesStepsIds[i].length; j++) {
                stepFakeValues.add(
                        createTestStepContentValues(
                                recipesStepsIds[i][j],
                                recipesStepsShortDesc[i][j],
                                recipesStepsVideos[i][j],
                                recipesStepsThumbnails[i][j],
                                recipeBakingIds[i]));
            }

            for (int k = 0; k < recipesIngredientsIngredient[i].length; k++) {
                ingredientFakeValues.add(
                        createTestIngredientContentValues(
                                recipesIngredientsIngredient[i][k],
                                recipesIngredientsQuantity[i][k],
                                recipesIngredientsMeasure[i][k],
                                recipeBakingIds[i]));
            }
        }

        context.getContentResolver().bulkInsert(BakingProvider.Recipe.RECIPE,
                recipeFakeValues.toArray(new  ContentValues[recipeBakingIds.length]));

        context.getContentResolver().bulkInsert(BakingProvider.Ingredient.INGREDIENT,
                ingredientFakeValues.toArray(new ContentValues[ingredientFakeValues.size()]));

        context.getContentResolver().bulkInsert(BakingProvider.Step.STEP,
                stepFakeValues.toArray(new ContentValues[stepFakeValues.size()]));
    }

}
