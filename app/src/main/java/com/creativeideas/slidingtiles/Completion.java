package com.creativeideas.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;

public class Completion extends BaseActivity implements View.OnClickListener {
    public static final String COMPLETED_IN = "completed_in";
    private InterstitialAd interstitialAd;
    private InterstitialAdListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_completion);

        /*interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitialUnitId));
        mListener = new InterstitialAdListener();
        interstitialAd.setAdListener(mListener);
        interstitialAd.loadAd(new AdRequest.Builder().build());*/

        String timeTaken = getIntent().getStringExtra(COMPLETED_IN);

        TextView timeTakenText = findViewById(R.id.message);
        timeTakenText.append(" " + timeTaken);

        MaterialButton button = findViewById(R.id.nAction);
        button.setOnClickListener(this);

        MaterialButton nButton = findViewById(R.id.pAction);
        nButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*mListener.setView(v);
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            handleInterstitial(v);
        }*/
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra(PauseMenu.OPTION_CODE, PauseMenu.REJECT);
        setResult(RESULT_OK, result);
        finish();
    }

    /*public void handleInterstitial(View v) {
        Intent result = new Intent();
        if (v.getId() == R.id.pAction) {
            result.putExtra(PauseMenu.OPTION_CODE, PauseMenu.CONFIRM);
        } else {
            result.putExtra(PauseMenu.OPTION_CODE, PauseMenu.REJECT);
        }
        setResult(RESULT_OK, result);
        finish();
    }

    private class InterstitialAdListener extends AdListener {
        private View view;

        void setView(View view) {
            this.view = view;
        }

        public void onAdClosed() {
            handleInterstitial(view);
        }
    }*/
}
