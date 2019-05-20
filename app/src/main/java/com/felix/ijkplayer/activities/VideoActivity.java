package com.felix.ijkplayer.activities;

import android.support.v7.app.AppCompatActivity;

import com.felix.ijkplayer.fragments.TracksFragment;

import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class VideoActivity extends AppCompatActivity implements TracksFragment.ITrackHolder {

    @Override
    public ITrackInfo[] getTrackInfo() {
        return new ITrackInfo[0];
    }

    @Override
    public int getSelectedTrack(int trackType) {
        return 0;
    }

    @Override
    public void selectTrack(int stream) {

    }

    @Override
    public void deselectTrack(int stream) {

    }
}
