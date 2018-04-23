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

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * BakingDatabase
 */
@Database(version = BakingDatabase.VERSION)
public class BakingDatabase {

    public static final int VERSION = 1;

    @Table(BakingContract.Recipe.class) public static final String RECIPE = "recipe";

    @Table(BakingContract.Step.class) public static final String STEP = "step";

    @Table(BakingContract.Ingredient.class) public static final String INGREDIENT = "ingredient";

}
