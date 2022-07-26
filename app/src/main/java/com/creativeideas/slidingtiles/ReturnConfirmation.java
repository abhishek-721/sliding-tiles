package com.creativeideas.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

public class ReturnConfirmation extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_quit);

        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.quit_to_main_menu);

        MaterialButton button = findViewById(R.id.nAction);
        button.setOnClickListener(this);

        MaterialButton nButton = findViewById(R.id.pAction);
        nButton.setOnClickListener(this);
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
