package com.creativeideas.slidingtiles;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class MainFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainFragmentLayout = inflater.inflate(R.layout.fragment_main, container, false);

        MaterialButton pbutton = mainFragmentLayout.findViewById(R.id.play);
        pbutton.setOnClickListener(this);

        MaterialButton qbutton = mainFragmentLayout.findViewById(R.id.quit);
        qbutton.setOnClickListener(this);

        MaterialButton shbutton = mainFragmentLayout.findViewById(R.id.share);
        shbutton.setOnClickListener(this);

        MaterialButton hbutton = mainFragmentLayout.findViewById(R.id.high);
        hbutton.setOnClickListener(this);

        MaterialButton sbutton = mainFragmentLayout.findViewById(R.id.setting);
        sbutton.setOnClickListener(this);

        return mainFragmentLayout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainFragment.OnFragmentInteractionListener) {
            mListener = (MainFragment.OnFragmentInteractionListener) context;
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
        if (mListener != null) {
            mListener.onMenuItemClicked(v);
        }
    }

    public interface OnFragmentInteractionListener {
        void onMenuItemClicked(View v);
    }
}
