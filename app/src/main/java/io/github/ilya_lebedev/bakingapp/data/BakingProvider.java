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
package io.github.ilya_lebedev.bakingapp.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * BakingProvider
 */
@ContentProvider(authority = BakingProvider.AUTHORITY, database = BakingDatabase.class)
public class BakingProvider {

    public static final String AUTHORITY = "io.github.ilya_lebedev.bakingapp";

    @TableEndpoint(table = BakingDatabase.RECIPE)
    public static class Recipe {

        @ContentUri(
                path = "recipe",
                type = "vnd.android.cursor.dir/recipe",
                defaultSort = BakingContract.Recipe.NAME + " ASC")
        public static final Uri RECIPE = Uri.parse("content://" + AUTHORITY + "/recipe");

        @InexactContentUri(
                path = "recipe/#",
                name = "RECIPE_WITH_BAKING_ID",
                type = "vnd.android.cursor.item/recipe",
                whereColumn = BakingContract.Recipe.BAKING_ID,
                pathSegment = 1)
        public static Uri withRecipeBakingId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/recipe/" + id);
        }

    }

    @TableEndpoint(table = BakingDatabase.INGREDIENT)
    public static class Ingredient {

        @ContentUri(
                path = "ingredient",
                type = "vnd.android.cursor.dir/ingredient",
                defaultSort = BakingContract.Ingredient._ID + "ASC")
        public static final Uri INGREDIENT = Uri.parse("content://" + AUTHORITY + "/ingredient");

        @InexactContentUri(
                path = "ingredient/recipe/#",
                name = "RECIPE_INGREDIENT",
                type = "vnd.android.cursor.dir/ingredient/recipe",
                whereColumn = BakingContract.Ingredient.BAKING_RECIPE_ID,
                pathSegment = 2)
        public static Uri withRecipeBakingId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/ingredient/recipe/" + id);
        }

    }

    @TableEndpoint(table = BakingDatabase.STEP)
    public static class Step {

        @ContentUri(
                path = "step",
                type = "vnd.android.cursor.dir/step",
                defaultSort = BakingContract.Step._ID + "ASC")
        public static final Uri STEP = Uri.parse("content://" + AUTHORITY + "/step");

        @InexactContentUri(
                path = "step/recipe/#",
                name = "RECIPE_STEP",
                type = "vnd.android.cursor.dir/step/recipe",
                whereColumn = BakingContract.Step.BAKING_RECIPE_ID,
                pathSegment = 2)
        public static Uri withRecipeBakingId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/step/recipe/" + id);
        }

    }

}
