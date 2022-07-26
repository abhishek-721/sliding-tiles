package com.creativeideas.slidingtiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

public class PauseMenu extends BaseActivity implements View.OnClickListener {
    public static final int CONFIRM = 1;
    public static final int REJECT = 0;

    public static final int REQUEST_CODE = 2;
    public static final String OPTION_CODE = "option_code";

    public static final int PENDING_RESTART = 3;
    public static final int PENDING_RETURN = 4;
    public static final int PENDING_NONE = 5;

    public static final int RESUMED=6;
    public static final int RESTART=7;
    public static final int RETURN=8;


    private int mPendingAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pause);

        MaterialButton rButton = findViewById(R.id.resume);
        rButton.setOnClickListener(this);

        MaterialButton rtButton = findViewById(R.id.restart);
        rtButton.setOnClickListener(this);

        MaterialButton mButton = findViewById(R.id.mainmenu);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        Intent confirmationIntent;
        switch (v.getId()) {
            case R.id.resume:
                setResultAndFinish(RESUMED);
                break;
            case R.id.restart:
                setPendingAction(PENDING_RESTART);
                confirmationIntent = new Intent(this, RestartConfirmation.class);
                startActivityForResult(confirmationIntent, REQUEST_CODE);
                break;
            case R.id.mainmenu:
                setPendingAction(PENDING_RETURN);
                confirmationIntent = new Intent(this, ReturnConfirmation.class);
                startActivityForResult(confirmationIntent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            int option = data.getIntExtra(OPTION_CODE, -1);
            onHandleResult(option);
        }
    }

    private void onHandleResult(int option) {
        if (option == CONFIRM && mPendingAction == PENDING_RESTART) {
            setResultAndFinish(RESTART);
        } else if (option == CONFIRM && mPendingAction == PENDING_RETURN) {
            setResultAndFinish(RETURN);
        } else {
            setPendingAction(PENDING_NONE);
        }
    }

    private void setPendingAction(int action) {
        mPendingAction = action;
    }

    private void setResultAndFinish(int option) {
        Intent result = new Intent();
        result.putExtra(OPTION_CODE, option);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
