package com.creativeideas.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

public class QuitConfirmation extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_quit);

        MaterialButton nAction = findViewById(R.id.nAction);
        nAction.setOnClickListener(this);

        MaterialButton pAction = findViewById(R.id.pAction);
        pAction.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        Intent result = new Intent();
        if (v.getId() == R.id.pAction) {
            result.putExtra(PauseMenu.OPTION_CODE, PauseMenu.CONFIRM);
        } else {
            result.putExtra(PauseMenu.OPTION_CODE, PauseMenu.REJECT);
        }
        setResult(RESULT_OK, result);
        finish();
    }
}
