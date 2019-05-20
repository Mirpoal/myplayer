package com.felix.ijkplayer.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.felix.ijkplayer.application.AppActivity;
import com.felix.ijkplayer.application.Settings;

import java.io.File;

public class FileExploreActivity extends AppActivity {
    @NonNull private Settings mSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mSettings == null) {
            mSettings = new Settings(this);
        }

        String lastDirectory = mSettings.getLastDirectory();
        if (!TextUtils.isEmpty(lastDirectory) && new File(lastDirectory).exists()) {

        }
    }

    private void doOpenDirectory(String path, boolean addToBackStack) {
        //Fragment fragment =
    }
}
