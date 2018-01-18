package com.example.android.droidchef.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.droidchef.R;
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

/**
 * Created by Astraeus on 1/15/2018.
 */

public class ExoPlayerFragment extends Fragment {

    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private Uri mMediaUri;
    private long mLastPosition = 0;


    public static final String LAST_POSITION_KEY = "lastPosition";
    public static final String LAST_MEDIA_URI = "mediaUri";


    // Mandatory empty constructor
    public ExoPlayerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){
            mLastPosition = savedInstanceState.getLong(LAST_POSITION_KEY);
            mMediaUri = Uri.parse(savedInstanceState.getString(LAST_MEDIA_URI));
        }

        // Inflate the exoplayer fragment layout
        View rootView = inflater.inflate(R.layout.fragment_exo_player, container, false);
        // Get a reference to the SimpleExoPlayerView in the fragment layout
        mExoPlayerView = rootView.findViewById(R.id.fragment_exo_player);

        return rootView;
    }

    public void initializeExoPlayer() {
        // Instantiate mExoPlayer
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

        String userAgent = Util.getUserAgent(getContext(), "DroidChef");
        MediaSource mediaSource = new ExtractorMediaSource(
                mMediaUri,
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null
        );
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.prepare(mediaSource, false, false);
        mExoPlayer.seekTo(mLastPosition);


    }

    public void releaseExoPlayerResources() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializeExoPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            initializeExoPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releaseExoPlayerResources();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releaseExoPlayerResources();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mLastPosition = mExoPlayer.getCurrentPosition();
        outState.putLong(LAST_POSITION_KEY, mLastPosition);
        outState.putString(LAST_MEDIA_URI, mMediaUri.toString());
    }

    public void setMediaUri(Uri uri) {
        mMediaUri = uri;
    }
}
