package com.creativeideas.slidingtiles;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class GameSettingsFragment extends Fragment {

    private SharedPreferences preferences;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);

        MaterialButton pop = view.findViewById(R.id.back);
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBackClicked(v);
                }
            }
        });

        TextView textView = view.findViewById(R.id.title);
        textView.setText(R.string.settings);

        MaterialButton sbutton = view.findViewById(R.id.b1);
        boolean isSoundEnabled = preferences.getBoolean("sound", true);
        if (isSoundEnabled) {
            sbutton.setText(R.string.sound_on);
        } else {
            sbutton.setText(R.string.sound_off);
        }
        sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                if (textView.getText().toString().equals(getString(R.string.sound_on))) {
                    textView.setText(R.string.sound_off);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("sound", false);
                    editor.apply();
                } else {
                    textView.setText(R.string.sound_on);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("sound", true);
                    editor.apply();
                }
            }
        });

        MaterialButton cbutton = view.findViewById(R.id.message);
        cbutton.setText(R.string.control_absolute);
        cbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                if (textView.getText().toString().equals(getString(R.string.control_absolute))) {
                    textView.setText(R.string.control_relative);
                } else {
                    textView.setText(R.string.control_absolute);
                }
            }
        });
        cbutton.setVisibility(View.GONE);

        MaterialButton rbutton = view.findViewById(R.id.b3);
        rbutton.setText(R.string.rate_on_store);
        rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri packageUri = Uri.parse("market://details?id=" + requireContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, packageUri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    requireActivity().startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    requireActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details/?id=" + requireContext().getPackageName())));
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HighFragment.OnFragmentInteractionListener) {
            mListener = (GameSettingsFragment.OnFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement GameSettings.OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void onBackClicked(View v);
    }
}
