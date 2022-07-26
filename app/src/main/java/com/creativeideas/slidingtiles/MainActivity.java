package com.creativeideas.slidingtiles;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.MobileAds;

public class MainActivity extends BaseActivity implements
        MainFragment.OnFragmentInteractionListener,
        CategoriesFragment.OnFragmentInteractionListener,
        ImageFragment.OnFragmentInteractionListener,
        ConfigurationFragment.OnFragmentInteractionListener,
        HighFragment.OnFragmentInteractionListener,
        GameSettingsFragment.OnFragmentInteractionListener,
        TilesFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        MobileAds.initialize(this);

        setContentView(R.layout.activity_main);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (savedInstanceState != null) {
            return;
        }

        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, mainFragment)
                .commit();
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMenuItemClicked(View v) {
        switch (v.getId()) {
            case R.id.play:
                CategoriesFragment categoriesFragment = new CategoriesFragment();
                openFragment(categoriesFragment);
                break;
            case R.id.high:
                HighFragment highFragment = new HighFragment();
                openFragment(highFragment);
                break;
            case R.id.setting:
                GameSettingsFragment gameSettingsFragment = new GameSettingsFragment();
                openFragment(gameSettingsFragment);
                break;
            case R.id.share:
                ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setChooserTitle("Share via")
                        .setText("Hey, Check out Sliding Tiles, It's fun to play http://play.google.com/store/apps/details?id=" + getPackageName())
                        .startChooser();
                break;
            case R.id.quit:
                Intent quitIntent = new Intent(this, QuitConfirmation.class);
                startActivityForResult(quitIntent, 3);
                break;
        }
    }

    @Override
    public void onCategorySelected(int id, int tag) {
        ImageFragment imageFragment = ImageFragment.newInstance(id, tag);
        openFragment(imageFragment);
    }

    @Override
    public void onImageSelected(int imgRes) {
        ConfigurationFragment configurationFragment = ConfigurationFragment.newInstance(imgRes);
        openFragment(configurationFragment);
    }

    @Override
    public void onConfigurationChanged(int tileCount, int resId) {
        TilesFragment tilesFragment = TilesFragment.newInstance(resId, tileCount);
        openFragment(tilesFragment);
    }

    @Override
    public void onPaused(View v) {
        Intent pauseIntent = new Intent(this, PauseMenu.class);
        startActivityForResult(pauseIntent, 1);
    }

    @Override
    public void onBackClicked(View v) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onComplete(String timeTaken) {
        Intent completionIntent = new Intent(this, Completion.class);
        completionIntent.putExtra(Completion.COMPLETED_IN, timeTaken);
        startActivityForResult(completionIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            int option = data.getIntExtra(PauseMenu.OPTION_CODE, -1);
            onHandlePauseMenu(option);
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            int option = data.getIntExtra(PauseMenu.OPTION_CODE, -1);
            if (option == PauseMenu.REJECT) {
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();
                manager.popBackStack();
            } else {
                Fragment tilesFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
                if (tilesFragment instanceof TilesFragment) {
                    ((TilesFragment) tilesFragment).onRestart();
                }
            }
        }

        if (requestCode == 3 && resultCode == Activity.RESULT_OK && data != null) {
            int option = data.getIntExtra(PauseMenu.OPTION_CODE, -1);
            if (option == PauseMenu.CONFIRM) {
                finish();
            }
        }

        if (requestCode == 4 && resultCode == Activity.RESULT_OK && data != null) {
            int option = data.getIntExtra(PauseMenu.OPTION_CODE, -1);
            if (option == PauseMenu.CONFIRM) {
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();
                manager.popBackStack();
            }
        }
    }

    private void onHandlePauseMenu(int option) {
        switch (option) {
            case PauseMenu.RESUMED:
                break;
            case PauseMenu.RESTART:
                Fragment tilesFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
                if (tilesFragment instanceof TilesFragment) {
                    ((TilesFragment) tilesFragment).onRestart();
                }
                break;
            case PauseMenu.RETURN:
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();
                manager.popBackStack();
                manager.popBackStack();
                manager.popBackStack();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment tilesFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (tilesFragment instanceof TilesFragment) {
            Intent quitIntent = new Intent(this, QuitConfirmation.class);
            startActivityForResult(quitIntent, 4);
        } else {
            super.onBackPressed();
        }
    }
}
