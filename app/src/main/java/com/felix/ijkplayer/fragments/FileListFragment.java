package com.felix.ijkplayer.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.felix.ijkplayer.R;
import com.felix.ijkplayer.content.PathCursor;

public class FileListFragment extends Fragment implements LoaderManager.LoaderCallbacks {
    private static final String ARG_PATH = "path";

    private TextView mPathView;
    private ListView mFileListView;
    private VideoAdapter mAdapter;

    private String mPath;

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        if (TextUtils.isEmpty(mPath))
            return null;

    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object o) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    private class VideoAdapter extends SimpleCursorAdapter {
        private class ViewHodler {
            public ImageView iconImageView;
            public TextView nameTextView;
        }

        public VideoAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2, null,
                    new String[]{PathCursor.CN_FILE_NAME, PathCursor.CN_FILE_PATH},
                    new int[]{android.R.id.text1, android.R.id.text2}, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.fragment_file_list_item, parent, false);
            }

            ViewHodler viewHodler = (ViewHodler) view.getTag();
            if (viewHodler == null) {
                viewHodler = new ViewHodler();
                viewHodler.iconImageView = view.findViewById(R.id.icon);
                viewHodler.nameTextView = view.findViewById(R.id.name);
            }

            if (isDirectory(position)) {
                viewHodler.iconImageView.setImageResource(R.drawable.ic_theme_folder);
            } else if (isVideo(position)) {
                viewHodler.iconImageView.setImageResource(R.drawable.ic_theme_play_arrow);
            } else {
                viewHodler.nameTextView.setText(getFileName(position));
            }

            return view;
        }

        @Override
        public long getItemId(int position) {
            Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return 0;
            return cursor.getLong(PathCursor.CI_ID);
        }

        Cursor moveToPosition(int position) {
            Cursor cursor = getCursor();
            if (cursor.getCount() == 0  || position >= cursor.getCount()) {
                return null;
            }
            cursor.moveToPosition(position);
            return cursor;
        }

        public boolean isDirectory(int position) {
            Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return true;
            return cursor.getInt(PathCursor.CI_IS_DIRECTORY) != 0;
        }

        public boolean isVideo(int position) {
            Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return true;
            return cursor.getInt(PathCursor.CI_IS_VIDEO) != 0;
        }

        public String getFileName(int position) {
            Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return "";
            return cursor.getString(PathCursor.CI_FILE_NAME);
        }

        public String getFilePath(int position) {
            Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return "";
            return cursor.getString(PathCursor.CI_FILE_PATH);
        }
    }


}
