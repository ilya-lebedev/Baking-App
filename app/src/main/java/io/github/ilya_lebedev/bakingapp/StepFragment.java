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

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import io.github.ilya_lebedev.bakingapp.data.BakingContract;

/**
 * StepFragment
 */
public class StepFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = StepFragment.class.getSimpleName();

    /*
     * The columns which is needed for displaying list of steps within RecipeFragment.
     */
    public static final String[] RECIPE_STEP_PROJECTION = {
            BakingContract.Step.DESCRIPTION,
            BakingContract.Step.VIDEO_URL
    };

    /*
     * This indices representing the values in the array of String above.
     * Uses for more quickly access to the data from query.
     * WARN: If the order or the contents of the Strings above changes,
     * these indices must be adjust to match the changes.
     */
    public static final int INDEX_STEP_DESCRIPTION = 0;
    public static final int INDEX_STEP_VIDEO_URL = 1;

    private static final int ID_STEP_LOADER = 98;

    private static final String KEY_STEP_URI = "step_uri";
    private static final String KEY_PLAYER_POSITION = "player_position";

    private Uri mStepUri;

    private View mPlayerContainer;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    private long mPlayerPosition = C.POSITION_UNSET;

    private TextView mDescriptionTv;

    // Mandatory empty constructor
    public StepFragment() {
    }

    // Inflates the view of step
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        mPlayerContainer = rootView.findViewById(R.id.playerViewContainer);

        mDescriptionTv = rootView.findViewById(R.id.tv_step_description);

        // Initialize the player view.
        mPlayerView = rootView.findViewById(R.id.playerView);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_STEP_URI)) {
                mStepUri = Uri.parse(savedInstanceState.getString(KEY_STEP_URI));
            }
            if (savedInstanceState.containsKey(KEY_PLAYER_POSITION)) {
                mPlayerPosition = savedInstanceState.getLong(KEY_PLAYER_POSITION);
            }
        }

        if (mStepUri == null) {
            rootView.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.content_view).setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.empty_view).setVisibility(View.GONE);
            rootView.findViewById(R.id.content_view).setVisibility(View.VISIBLE);

            getLoaderManager().initLoader(ID_STEP_LOADER, null, this);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_STEP_URI, mStepUri.toString());

        long position = C.POSITION_UNSET;
        if (mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
        }
        outState.putLong(KEY_PLAYER_POSITION, position);

        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case ID_STEP_LOADER: {
                return new CursorLoader(getContext(),
                        mStepUri,
                        RECIPE_STEP_PROJECTION,
                        null,
                        null,
                        null);
            }

            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int loaderId = loader.getId();

        switch (loaderId) {

            case ID_STEP_LOADER: {
                if (cursor == null || !cursor.moveToFirst()) {
                    return;
                }

                String description = cursor.getString(INDEX_STEP_DESCRIPTION);
                String videoUrl = cursor.getString(INDEX_STEP_VIDEO_URL);

                mDescriptionTv.setText(description);
                if (videoUrl != null) {
                    initializePlayer(Uri.parse(videoUrl));
                } else {
//                    mPlayerView
                    mPlayerContainer.setVisibility(View.GONE);
                }

                break;
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Nothing to do here
    }

    public void setRecipeUri(Uri recipeUri) {
        mStepUri = recipeUri;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            if (mPlayerPosition != C.POSITION_UNSET) {
                mExoPlayer.seekTo(mPlayerPosition);
            }
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}
