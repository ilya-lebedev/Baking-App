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

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * BakingContract
 */
public class BakingContract {

    public interface Recipe {

        @DataType(INTEGER)
        @PrimaryKey
        @AutoIncrement
        String _ID = "_id";

        @DataType(INTEGER)
        @NotNull
        @Unique(onConflict = ConflictResolutionType.REPLACE)
        String BAKING_ID = "baking_id";

        @DataType(TEXT)
        @NotNull
        String NAME = "name";

    }

    public interface Ingredient {

        @DataType(INTEGER)
        @PrimaryKey
        @AutoIncrement
        String _ID = "_id";

        @DataType(INTEGER)
        @NotNull
        String QUANTITY = "quantity";

        @DataType(TEXT)
        @NotNull
        String MEASURE = "measure";

        @DataType(TEXT)
        @NotNull
        String INGREDIENT = "ingredient";

        @DataType(INTEGER)
        @NotNull
        String BAKING_RECIPE_ID = "baking_recipe_id";

    }

    public interface Step {

        @DataType(INTEGER)
        @PrimaryKey
        @AutoIncrement
        String _ID = "_id";

        @DataType(INTEGER)
        @NotNull
        @Unique(onConflict = ConflictResolutionType.REPLACE)
        String BAKING_ID = "baking_id";

        @DataType(TEXT)
        @NotNull
        String SHORT_DESCRIPTION = "short_description";

        @DataType(TEXT)
        @NotNull
        String DESCRIPTION = "description";

        @DataType(TEXT)
        String VIDEO_URL = "video_url";

        @DataType(TEXT)
        String THUMBNAIL_URL = "thumbnail_url";

        @DataType(INTEGER)
        @NotNull
        String BAKING_RECIPE_ID = "baking_recipe_id";

    }

}
