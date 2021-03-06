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
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * {@link RecipeStepAdapter} exposes a list of steps
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}
 */
public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    private WeakReference<View> mSelectedView;
    private int mSelectedPosition = -1;

    final private RecipeStepAdapter.RecipeStepAdapterOnClickHandler mClickHandler;

    public RecipeStepAdapter(@NonNull Context context,
                             RecipeStepAdapter.RecipeStepAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public RecipeStepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_step, parent, false);
        view.setFocusable(true);

        return new RecipeStepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String shortDescription = mCursor.getString(RecipeFragment.INDEX_STEP_SHORT_DESCRIPTION);

        holder.stepShortDescription.setText(shortDescription);
        if (position == mSelectedPosition) {
            holder.itemView.setSelected(true);
            if (mSelectedView != null && mSelectedView.get() != null) {
                mSelectedView.get().setSelected(false);
            }
            mSelectedView = new WeakReference<>(holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * Swap the cursor used by RecipeStepAdapter for its steps data.
     *
     * @param cursor the new cursor to use as RecipeStepAdapter's data source
     */
    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public void setPosition(int position) {
        mSelectedPosition = position;
    }

    /**
     * RecipeStepAdapterOnClickHandler
     * The interface that receives onClick messages.
     */
    public interface RecipeStepAdapterOnClickHandler {
        void onClick(int stepBakingId, int adapterSelectedPosition);
    }

    /**
     * RecipeStepAdapterViewHolder
     */
    class RecipeStepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView stepShortDescription;

        public RecipeStepAdapterViewHolder(View itemView) {
            super(itemView);

            stepShortDescription = itemView.findViewById(R.id.tv_step_short_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v.setSelected(true);

            if (mSelectedView != null && mSelectedView.get() != null) {
                mSelectedView.get().setSelected(false);
            }

            mSelectedView = new WeakReference<>(v);
            int adapterPosition = getAdapterPosition();
            mSelectedPosition = adapterPosition;

            mCursor.moveToPosition(adapterPosition);
            int stepBakingId = mCursor.getInt(RecipeFragment.INDEX_STEP_ID);
            mClickHandler.onClick(stepBakingId, adapterPosition);
        }

    }

}
