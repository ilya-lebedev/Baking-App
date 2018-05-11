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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.CheckBox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.ilya_lebedev.bakingapp.data.BakingPreferences;
import io.github.ilya_lebedev.bakingapp.data.BakingProvider;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;

/**
 * RecipeActivityTest
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    private static final int ITEM = 5;
    private static final int RECIPE_BAKING_ID = 2;

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<RecipeActivity>(RecipeActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, RecipeActivity.class);
                    Uri uri = BakingProvider.Recipe.withRecipeBakingId(RECIPE_BAKING_ID);
                    intent.setData(uri);
                    return intent;
                }
            };

    @Test
    public void clickStepItem_OpensStepActivity() {

        onView(withId(R.id.nsv_recipe_container))
                .perform(ViewActions.swipeUp());

        onView(withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM, click()));
    }

    @Test
    public void clickDesiredMenuItem_ChangesMenuItemState() {

        Context targetContext = getInstrumentation()
                .getTargetContext();

        int desiredRecipeId = BakingPreferences.getDesiredRecipe(targetContext);
        boolean isDesired = desiredRecipeId == RECIPE_BAKING_ID;

        openActionBarOverflowOrOptionsMenu(targetContext);
        ViewInteraction view = onView(allOf(
                instanceOf(CheckBox.class),
                hasSibling(withChild(withText(R.string.action_desired_recipe))),
                isCompletelyDisplayed()));
        if (isDesired) {
            view.check(matches(isChecked()));
        } else {
            view.check(matches(not(isChecked())));
        }

        view.perform(click());

        openActionBarOverflowOrOptionsMenu(targetContext);
        view = onView(allOf(
                instanceOf(CheckBox.class),
                hasSibling(withChild(withText(R.string.action_desired_recipe))),
                isCompletelyDisplayed()));
        if (isDesired) {
            view.check(matches(not(isChecked())));
        } else {
            view.check(matches(isChecked()));
        }

    }

}
