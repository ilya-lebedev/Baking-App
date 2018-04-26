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
package io.github.ilya_lebedev.bakingapp.sync;

import android.content.ContentResolver;
import android.content.Context;

import java.net.URL;

import io.github.ilya_lebedev.bakingapp.data.BakingData;
import io.github.ilya_lebedev.bakingapp.data.BakingProvider;
import io.github.ilya_lebedev.bakingapp.utilities.BakingJsonUtils;
import io.github.ilya_lebedev.bakingapp.utilities.BakingNetworkUtils;

/**
 * BakingSyncTask
 */
public class BakingSyncTask {

    public static void syncBakingData(Context context) {
        URL bakingDataRequestUrl = BakingNetworkUtils.getRecipeListUrl();

        try {
            String bakingDataJsonString = BakingNetworkUtils
                    .getResponseFromHttpUrl(bakingDataRequestUrl);

            BakingData bakingData = BakingJsonUtils.getBakingDataFromJson(bakingDataJsonString);

            if (bakingData.recipesContentValues != null
                    && bakingData.recipesContentValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(
                        BakingProvider.Recipe.RECIPE,
                        null,
                        null);
                contentResolver.delete(
                        BakingProvider.Step.STEP,
                        null,
                        null);
                contentResolver.delete(
                        BakingProvider.Ingredient.INGREDIENT,
                        null,
                        null);

                contentResolver.bulkInsert(
                        BakingProvider.Recipe.RECIPE,
                        bakingData.recipesContentValues);
                contentResolver.bulkInsert(
                        BakingProvider.Step.STEP,
                        bakingData.stepsContentValues);
                contentResolver.bulkInsert(
                        BakingProvider.Ingredient.INGREDIENT,
                        bakingData.ingredientsContentValues);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
