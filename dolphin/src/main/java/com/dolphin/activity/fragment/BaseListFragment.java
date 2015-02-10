package com.dolphin.activity.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class BaseListFragment<T extends ListView, A extends ListAdapter>
        extends BaseFragment implements AdapterView.OnItemClickListener {
    protected T listView;
    protected A adapter;

    protected int selectionItem;
    protected int top;

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listView = (T) inflater.inflate(layout(), null);
        listView.setOnItemClickListener(this);
        return listView;
    }

    @Override
    public void onPause() {
        selectionItem = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        listView.setSelectionFromTop(selectionItem, top);
    }

    protected void setListAdapter(A adapter) {
        this.adapter = adapter;
        listView.setAdapter(adapter);
    }

    protected abstract int layout();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
