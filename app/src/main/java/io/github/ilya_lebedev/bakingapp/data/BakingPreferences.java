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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.github.ilya_lebedev.bakingapp.R;

/**
 * BakingPreferences
 */
public class BakingPreferences {

    /* This is utility class and we don't need to instantiate it */
    private BakingPreferences() {}

    public static int getDesiredRecipe(Context context) {

        String key = context.getString(R.string.pref_desired_recipe_key);
        int defaultValue = context.getResources()
                .getInteger(R.integer.pref_desired_recipe_default);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getInt(key, defaultValue);
    }

    public static void setDesiredRecipe(Context context, int recipeBakingId) {

        String key = context.getString(R.string.pref_desired_recipe_key);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(key, recipeBakingId);
        editor.apply();
    }

}
