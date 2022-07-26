package com.creativeideas.slidingtiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class HighFragment extends Fragment {

    private long easy;
    private long medium;
    private long hard;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        easy = preferences.getLong(String.valueOf(4), Long.MAX_VALUE);
        medium = preferences.getLong(String.valueOf(5), Long.MAX_VALUE);
        hard = preferences.getLong(String.valueOf(6), Long.MAX_VALUE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high, container, false);

        MaterialButton pop = view.findViewById(R.id.back);
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBackClicked(v);
                }
            }
        });

        if (!(easy == Long.MAX_VALUE)) {
            TextView value1 = view.findViewById(R.id.value1);
            value1.setText(formatLong(easy));
        }

        if (!(medium == Long.MAX_VALUE)) {
            TextView value2 = view.findViewById(R.id.value2);
            value2.setText(formatLong(medium));
        }

        if (!(hard == Long.MAX_VALUE)) {
            TextView value3 = view.findViewById(R.id.value3);
            value3.setText(formatLong(hard));
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HighFragment.OnFragmentInteractionListener) {
            mListener = (HighFragment.OnFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement HighFragment.OnFragmentInteractionListener");
        }
    }

    public static String formatLong(long time) {
        double timeTaken = (double) time;
        timeTaken = timeTaken / 1000;
        long sec = (long) timeTaken;
        return DateUtils.formatElapsedTime(sec);
    }

    public interface OnFragmentInteractionListener {
        void onBackClicked(View v);
    }
}
