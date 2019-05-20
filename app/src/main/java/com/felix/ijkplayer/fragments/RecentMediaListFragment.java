package com.felix.ijkplayer.fragments;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.felix.ijkplayer.R;
import com.felix.ijkplayer.content.RecentMediaStorage;

public class RecentMediaListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView mFileListView;
    private RecentMediaAdapter mediaAdapter;

    public static RecentMediaListFragment newInstance() {
        RecentMediaListFragment fragment = new RecentMediaListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_file_list, container, false);
        mFileListView = viewGroup.findViewById(R.id.file_list_view);
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();

        mediaAdapter = new RecentMediaAdapter(activity);
        mFileListView.setAdapter(mediaAdapter);
        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String url = mediaAdapter.getUrl(position);
                String name = mediaAdapter.getName(position);
                // start videoactivity
                //VideoActivity.intentTo(activity, url, name);
            }
        });
    }

    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new RecentMediaStorage.CursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    final class RecentMediaAdapter extends SimpleCursorAdapter {
        private int mIndex_id = -1;
        private int mIndex_url = -1;
        private int mIndex_name = -1;

        public RecentMediaAdapter(Context context) {
            super(context, R.layout.simple_list_item_2, null,
                    new String[]{RecentMediaStorage.Entry.COLUMN_NAME_NAME, RecentMediaStorage.Entry.COLUMN_NAME_URL},
                    new int[]{R.id.text1, R.id.text2}, 0);
        }

        @Override
        public Cursor swapCursor(Cursor c) {
            Cursor res = super.swapCursor(c);
            mIndex_id = c.getColumnIndex(RecentMediaStorage.Entry.COLUMN_NAME_ID);
            mIndex_url = c.getColumnIndex(RecentMediaStorage.Entry.COLUMN_NAME_URL);
            mIndex_name = c.getColumnIndex(RecentMediaStorage.Entry.COLUMN_NAME_NAME);
            return res;
        }

        @Override
        public long getItemId(int position) {
            final Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return 0;

            return cursor.getLong(mIndex_id);
        }

        Cursor moveToPosition(int position) {
            final Cursor cursor = getCursor();
            if (cursor.getCount() == 0 || position >= cursor.getCount()) {
                return null;
            }
            cursor.moveToPosition(position);
            return cursor;
        }

        public String getUrl(int position) {
            final Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return "";

            return cursor.getString(mIndex_url);
        }

        public String getName(int position) {
            final Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return "";

            return cursor.getString(mIndex_name);
        }
    }
}
