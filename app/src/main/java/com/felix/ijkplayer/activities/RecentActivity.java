package com.felix.ijkplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.felix.ijkplayer.application.AppActivity;

public class RecentActivity extends AppActivity {
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RecentActivity.class);
        return intent;
    }

    public static void intentTo(Context context) {
        context.startActivity(newIntent(context));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fra
    }
}
