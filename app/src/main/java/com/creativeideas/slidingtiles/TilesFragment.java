package com.creativeideas.slidingtiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TilesFragment extends Fragment implements TileView.TileViewListener, View.OnClickListener {

    private static final String ARG_IMGRES = "param1";
    private static final String ARG_GRIDSIZE = "param2";

    private int mImageRes;
    private int mTileCount;

    private TileView mTileView;
    private Chronometer mChronometer;
    private SoundPool mSoundPool;

    private long mPauseOffset;
    private boolean isSoundEnabled;
    private int swapSound;

    private OnFragmentInteractionListener mListener;

    public static TilesFragment newInstance(int resId, int TileCount) {
        TilesFragment fragment = new TilesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMGRES, resId);
        args.putInt(ARG_GRIDSIZE, TileCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageRes = getArguments().getInt(ARG_IMGRES);
            mTileCount = getArguments().getInt(ARG_GRIDSIZE);
        }

        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        isSoundEnabled = preferences.getBoolean("sound", true);

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(attributes)
                .build();

        swapSound = mSoundPool.load(requireActivity(), R.raw.button, 1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tiles, container, false);

        mTileView = new TileView(getContext(), mImageRes, mTileCount);
        mTileView.setFocusable(true);
        mTileView.initNew();
        mTileView.setTileViewListener(this);

        mChronometer = view.findViewById(R.id.chronometer);

        ImageButton pause = view.findViewById(R.id.pause);
        pause.setOnClickListener(this);

        FrameLayout frameLayout = view.findViewById(R.id.container);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(mTileView, params);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mChronometer.setBase(SystemClock.elapsedRealtime() - mPauseOffset);
        mChronometer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mChronometer.stop();
        mPauseOffset = SystemClock.elapsedRealtime() - mChronometer.getBase();
    }

    public void onRestart() {
        mTileView.initNew();
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mPauseOffset = 0;
        mTileView.invalidate();
    }

    public void onSwap() {
        if (isSoundEnabled) {
            mSoundPool.play(swapSound, 1, 1, 0, 0, 1);
        }
    }

    @Override
    public void onSolved() {
        mChronometer.stop();
        mPauseOffset = SystemClock.elapsedRealtime() - mChronometer.getBase();
        saveData();
        String timeString = HighFragment.formatLong(mPauseOffset);
        mListener.onComplete(timeString);
    }

    private void saveData() {
        String key = String.valueOf(mTileCount);
        long timeTaken = mPauseOffset;
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        long highScore = preferences.getLong(key, Long.MAX_VALUE);

        if (timeTaken < highScore) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, timeTaken);
            editor.apply();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainFragment.OnFragmentInteractionListener) {
            mListener = (TilesFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        mListener.onPaused(v);
    }

    public interface OnFragmentInteractionListener {
        void onPaused(View v);

        void onComplete(String timeTaken);
    }
}