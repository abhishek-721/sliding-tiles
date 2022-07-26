package com.creativeideas.slidingtiles;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigurationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigurationFragment extends Fragment {
    private static final String ARG_RES_ID = "param1";

    private int mResId;

    private OnFragmentInteractionListener mListener;

    public ConfigurationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imgRes Parameter 1.
     * @return A new instance of fragment ConfigurationFragment.
     */
    public static ConfigurationFragment newInstance(int imgRes) {
        ConfigurationFragment fragment = new ConfigurationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RES_ID, imgRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResId = getArguments().getInt(ARG_RES_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);

        MaterialButton easy = view.findViewById(R.id.b1);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(4);
            }
        });

        MaterialButton medium = view.findViewById(R.id.message);
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(5);
            }
        });

        MaterialButton hard = view.findViewById(R.id.b3);
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(6);
            }
        });

        MaterialButton back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBackClicked(v);
            }
        });

        return view;
    }

    private void onButtonPressed(int tileCount) {
        if (mListener != null) {
            mListener.onConfigurationChanged(tileCount, mResId);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ConfigurationFragment.OnFragmentInteractionListener) {
            mListener = (ConfigurationFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ConfigurationFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onConfigurationChanged(int tileCount, int resId);

        void onBackClicked(View v);
    }
}
