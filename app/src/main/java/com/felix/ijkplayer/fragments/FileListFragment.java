package com.felix.ijkplayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.felix.ijkplayer.R;
import com.felix.ijkplayer.content.PathCursor;
import com.felix.ijkplayer.content.PathCursorLoader;
import com.felix.ijkplayer.eventbus.FileExplorerEvents;

import java.io.File;

public class FileListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_PATH = "path";

    private TextView mPathView;
    private ListView mFileListView;
    private VideoAdapter mAdapter;

    private String mPath;

    public static FileListFragment newInstance(String path) {
        FileListFragment f = new FileListFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_file_list, container, false);
        mPathView = viewGroup.findViewById(R.id.path_view);
        mFileListView = viewGroup.findViewById(R.id.file_list_view);
        mPathView.setVisibility(View.VISIBLE);
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPath = bundle.getString(ARG_PATH);
            mPath = new File(mPath).getAbsolutePath();
            mPathView.setText(mPath);
        }
        mAdapter = new VideoAdapter(activity);
        mFileListView.setAdapter(mAdapter);
        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String path = mAdapter.getFilePath(position);
                if (TextUtils.isEmpty(path))
                    return;
                FileExplorerEvents.getBus().post(new FileExplorerEvents.OnClickFile(path));
            }
        });

        getLoaderManager().initLoader(1, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (TextUtils.isEmpty(mPath))
            return null;
        return new PathCursorLoader(getActivity(), mPath);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    final class VideoAdapter extends SimpleCursorAdapter {
        final class ViewHolder {
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

            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.iconImageView = view.findViewById(R.id.icon);
                viewHolder.nameTextView = view.findViewById(R.id.name);
            }

            if (isDirectory(position)) {
                viewHolder.iconImageView.setImageResource(R.drawable.ic_theme_folder);
            } else if (isVideo(position)) {
                viewHolder.iconImageView.setImageResource(R.drawable.ic_theme_play_arrow);
            } else {
                viewHolder.iconImageView.setImageResource(R.drawable.ic_theme_description);
            }
            viewHolder.nameTextView.setText(getFileName(position));

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
