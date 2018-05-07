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
package io.github.ilya_lebedev.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import io.github.ilya_lebedev.bakingapp.R;

/**
 * BakingWidgetService
 */
public class BakingWidgetService extends IntentService {

    public static final String ACTION_UPDATE_BAKING_WIDGETS =
            "io.github.ilya_lebedev.bakingapp.action.update_baking_widgets";

    /**
     * Creates an IntentService.
     */
    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    public static void startActionUpdateBakingWidgets(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_UPDATE_BAKING_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_BAKING_WIDGETS.equals(action)) {
                handleActionUpdateBakingWidgets();
            }
        }

    }

    /**
     * Handle action UpdateBakingWidgets in the provided background thread
     */
    private void handleActionUpdateBakingWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, appWidgetIds);
    }

}
